using System.Collections.Generic;
using System.Linq;
using System;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Text;
using System.Runtime.InteropServices;
using com.sas1946.il2.directx.dinput.ThreadQueue;

namespace com.sas1946.il2.directx.dinput.DeviceLinkClient
{
    // State object for receiving data from remote device.
    public class StateObject
    {
        // Client socket.
        public Socket workSocket = null;
        // Size of receive buffer.
        public const int BufferSize = 256;
        // Receive buffer.
        public byte[] buffer = new byte[BufferSize];
        // Received data string.
        public StringBuilder sb = new StringBuilder();
    }

    public class UdpState : Object
    {
        public UdpState(IPEndPoint theIpEndPoint, MyUdpClient theUdpClient)
        {
            this.ipEndPoint = theIpEndPoint;
            this.udpClient = theUdpClient;
        }
        public IPEndPoint ipEndPoint;
        public MyUdpClient udpClient;
    }

    public class MyUdpClient : UdpClient
    {
        public bool IsDead { get; set; }
        protected override void Dispose(bool disposing)
        {
            IsDead = true;
            base.Dispose(disposing);
        }
    }


    public class DeviceLinkClient
    {

        [DllImport("user32.dll")]
        static extern bool PostMessage(IntPtr hWnd, uint Msg, int wParam, int lParam);
        // ManualResetEvent instances signal completion.
        private static AutoResetEvent syncDone =
            new AutoResetEvent(false);
        private static AutoResetEvent connectionAlive =
            new AutoResetEvent(false);
        private static AutoResetEvent connectionThreadFinished =
            new AutoResetEvent(false);

        public const string SYNC_REQUEST = "R/2";
        public const string SYNC_REPLY = "A/2\\";

        // The response from the remote device.
        private static String response = String.Empty;

        MyUdpClient myUdpClient = new MyUdpClient();
        IPEndPoint clientSocketEndPoint;
        private IAsyncResult currentAsyncResult = null;
        private string ipAddress;
        private int port;
        private ThreadQueue<string> rcvQueue;
        private ThreadQueue<string> sndQueue;
        private IntPtr updateMessageWindow;
        private int updateMessage;
        private int connectedMessage;
        public const int PACKET_RECEIVED = 0;
        public const int PACKET_SENT = 1;
        private bool isDead = false;

        public DeviceLinkClient(string theIpAddress, int thePort, ThreadQueue<string> theRcvQueue, ThreadQueue<string> theSndQueue, IntPtr theUpdateMessageWindow, int theUpdateMessage, int theConnectedMessage)
        {
            this.ipAddress = theIpAddress;
            this.port = thePort;
            this.rcvQueue = theRcvQueue;
            this.sndQueue = theSndQueue;
            this.updateMessageWindow = theUpdateMessageWindow;
            this.updateMessage = theUpdateMessage;
            this.connectedMessage = theConnectedMessage;
        }

        public void Open()
        {
            // Connect to a remote device.
            try
            {

                // Establish the remote endpoint for the socket.
                // The name of the 
                // remote device is "host.contoso.com".
                this.clientSocketEndPoint = new IPEndPoint(IPAddress.Parse(this.ipAddress), this.port);

                //// Connect to the remote endpoint.
                //this.myUdpClient.Connect(this.clientSocketEndPoint);

                //UdpState udpState = new UdpState(this.clientSocketEndPoint, this.myUdpClient);

                //this.currentAsyncResult = this.myUdpClient.BeginReceive(new AsyncCallback(ReceiveCallback), udpState);

                Thread t = new Thread(new ThreadStart(establishConnectionThread));
                t.IsBackground = true;
                t.Start();

            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
        }

        public void Close()
        {
            this.isDead = true;
            if (this.myUdpClient != null)
            {
                this.myUdpClient.Close();
            }
            connectionThreadFinished.WaitOne(1000);
        }

        private void establishConnectionThread()
        {
            while (!this.isDead)
            {
                if (this.isDead) break;
                do
                {
                    // Create a TCP/IP socket.
                    this.myUdpClient = new MyUdpClient();
                    // Connect to the remote endpoint.
                    this.myUdpClient.Connect(this.clientSocketEndPoint);
                    UdpState udpState = new UdpState(this.clientSocketEndPoint, this.myUdpClient);
                    this.currentAsyncResult = this.myUdpClient.BeginReceive(new AsyncCallback(ReceiveCallback), udpState);
                    this.Send(SYNC_REQUEST);
                    if (syncDone.WaitOne(100)) break;
                    this.myUdpClient.Close();
                } while (!this.isDead);
                if (this.isDead) break;
                PostMessage(this.updateMessageWindow, (uint)this.connectedMessage, 1, 0);
                do
                {
                    if (connectionAlive.WaitOne(500)) continue;
                    if (this.isDead) break;
                    this.Send(SYNC_REQUEST);
                    if (!syncDone.WaitOne(5000)) break;
                } while (!this.isDead);
                if (this.isDead) break;
                try
                {
                    this.myUdpClient.Close();
                }
                catch (Exception) { }
                PostMessage(this.updateMessageWindow, (uint)this.connectedMessage, 0, 0);
            }
            connectionThreadFinished.Set();
        }

        public void Send(string data)
        {
            this.Send(this.myUdpClient, data);
        }

        private void ReceiveCallback(IAsyncResult ar)
        {
            try
            {
                if (ar == this.currentAsyncResult)
                {
                    MyUdpClient udpClient = (MyUdpClient)((UdpState)(ar.AsyncState)).udpClient;
                    IPEndPoint ipEndPoint = (IPEndPoint)((UdpState)(ar.AsyncState)).ipEndPoint;
                    if (udpClient.IsDead) return;
                    Byte[] buffer = udpClient.EndReceive(ar, ref ipEndPoint);
                    if (buffer.Length > 0)
                    {
                        string receivedTelegram = System.Text.Encoding.ASCII.GetString(buffer);
                        if (receivedTelegram.StartsWith("A/2\\"))
                        {
                            syncDone.Set();
                        }
                        else
                        {
                            this.rcvQueue.Enqueue(receivedTelegram);
                            PostMessage(this.updateMessageWindow, (uint)this.updateMessage, PACKET_RECEIVED, 0);
                        }
                        connectionAlive.Set();
                    }
                    UdpState udpState = new UdpState(ipEndPoint, udpClient);
                    this.currentAsyncResult = udpClient.BeginReceive(new AsyncCallback(ReceiveCallback), udpState);
                }
            }
            catch (SocketException)
            {

            }
            catch (ObjectDisposedException)
            {

            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
        }

        private void Send(MyUdpClient udpClient, String data)
        {
            byte[] byteData = Encoding.ASCII.GetBytes(data);
            UdpState udpState = new UdpState(this.clientSocketEndPoint, udpClient);
            udpClient.BeginSend(byteData, byteData.Length, new AsyncCallback(SendCallback), udpState);
            this.sndQueue.Enqueue(data);
        }

        private void SendCallback(IAsyncResult ar)
        {
            try
            {
                MyUdpClient udpClient = (MyUdpClient)((UdpState)(ar.AsyncState)).udpClient;
                IPEndPoint ipEndPoint = (IPEndPoint)((UdpState)(ar.AsyncState)).ipEndPoint;
                if (udpClient.IsDead) return;
                int bytesSent = udpClient.EndSend(ar);
                PostMessage(this.updateMessageWindow, (uint)this.updateMessage, PACKET_SENT, 0);
                //byte[] recv = udpClient.Receive(ref ipEndPoint);

                //UdpState udpState = new UdpState(this.clientSocketEndPoint, udpClient);
                //this.currentAsyncResult = udpClient.BeginReceive(new AsyncCallback(ReceiveCallback), udpState);
            }
            catch (SocketException)
            {

            }
            catch (ObjectDisposedException)
            {

            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
        }

    }
}
