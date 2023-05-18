package model;

public class QueueObj {
    public enum Source {
        GUI, SOCKET, EVENTLOG
    }

    private Source     source;
    private String     data;
    private long       eventTime;
    private GUICommand guiCommand;

    public QueueObj(Source source, String data, long eventTime) {
        this.source = source;
        this.data = data;
        this.eventTime = eventTime;
    }

    public QueueObj(Source source, GUICommand guiCommand) {
        this.source = source;
        this.guiCommand = guiCommand;
    }

    public Source getSource() {
        return source;
    }

    public String getData() {
        return data;
    }

    public long getEventTime() {
        return eventTime;
    }

    public GUICommand getGuiCommand() {
        return guiCommand;
    }
}
