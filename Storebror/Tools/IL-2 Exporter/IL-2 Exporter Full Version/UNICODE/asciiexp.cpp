//************************************************************************** 
//* Asciiexp.cpp	- Ascii File Exporter
//* 
//* By Christer Janson
//* Kinetix Development
//*
//* January 20, 1997 CCJ Initial coding
//*
//* This module contains the DLL startup functions
//*
//* Copyright (c) 1997, All Rights Reserved. 
//***************************************************************************
#include "asciiexp.h"

#include "3dsmaxport.h"

HINSTANCE hInstance;

static BOOL showPrompts;
static BOOL exportSelected;
static TCHAR szExt1[128];
static TCHAR szExt2[128];

// Class ID. These must be unique and randomly generated!!
// If you use this as a sample project, this is the first thing
// you should change!
#define ASCIIEXP_CLASS_ID	Class_ID(0x8f45ae1a, 0x4a495a0e)

//#define DEBUG
#define DEBUG_LOD
//#define DEBUG_COLL

BOOL WINAPI DllMain(HINSTANCE hinstDLL,ULONG fdwReason,LPVOID lpvReserved) 
{
   if( fdwReason == DLL_PROCESS_ATTACH )
   {
      hInstance = hinstDLL; 
      DisableThreadLibraryCalls(hInstance);
	  _tcscpy_s(szExt1, sizeof(szExt1) / sizeof(TCHAR), GetString(IDS_EXTENSION1));
	  _tcscpy_s(szExt2, sizeof(szExt2) / sizeof(TCHAR), GetString(IDS_EXTENSION2));
   }

	return (TRUE);
}


__declspec( dllexport ) const TCHAR* LibDescription() 
{
	return GetString(IDS_LIBDESCRIPTION);
}

/// MUST CHANGE THIS NUMBER WHEN ADD NEW CLASS 
__declspec( dllexport ) int LibNumberClasses() 
{
	return 1;
}


__declspec( dllexport ) ClassDesc* LibClassDesc(int i) 
{
	switch(i) {
	case 0: return GetAsciiExpDesc();
	default: return 0;
	}
}

__declspec( dllexport ) ULONG LibVersion() 
{
	return VERSION_3DSMAX;
}

// Let the plug-in register itself for deferred loading
__declspec( dllexport ) ULONG CanAutoDefer()
{
	return 1;
}

class AsciiExpClassDesc:public ClassDesc {
public:
	int				IsPublic() { return 1; }
	void*			Create(BOOL loading = FALSE) { return new AsciiExp; } 
	const TCHAR*	ClassName() { return GetString(IDS_ASCIIEXP); }
	SClass_ID		SuperClassID() { return SCENE_EXPORT_CLASS_ID; } 
	Class_ID		ClassID() { return ASCIIEXP_CLASS_ID; }
	const TCHAR*	Category() { return GetString(IDS_CATEGORY); }
};

static AsciiExpClassDesc AsciiExpDesc;

ClassDesc* GetAsciiExpDesc()
{
	return &AsciiExpDesc;
}

TCHAR *GetString(int id)
{
	static TCHAR buf[256];

	if (hInstance)
		return LoadString(hInstance, id, buf, sizeof(buf)/sizeof(TCHAR)) ? buf : NULL;

	return NULL;
}

AsciiExp::AsciiExp()
{
	// These are the default values that will be active when 
	// the exporter is ran the first time.
	// After the first session these options are sticky.
	bIncludeMesh = TRUE;
	bIncludeAnim = TRUE;
	bIncludeMtl =  TRUE;
	bIncludeMeshAnim =  FALSE;
	bIncludeCamLightAnim = FALSE;
	bIncludeIKJoints = FALSE;
	bIncludeNormals  =  FALSE;
	bIncludeTextureCoords = FALSE;
	bIncludeVertexColors = FALSE;
	bIncludeObjGeom = TRUE;
	bIncludeObjShape = TRUE;
	bIncludeObjCamera = TRUE;
	bIncludeObjLight = TRUE;
	bIncludeObjHelper = TRUE;
	bAlwaysSample = FALSE;
	nKeyFrameStep = 5;
	nMeshFrameStep = 5;
	nPrecision = 6;
	nStaticFrame = 0;
}

AsciiExp::~AsciiExp()
{
}

int AsciiExp::ExtCount()
{
	return 2;
}

const TCHAR * AsciiExp::Ext(int n)
{
	switch(n) {
	case 0:
		// This cause a static string buffer overwrite
		// Fixed by SAS~Storebror: This issue only occurs if you dynamically load the string resource at this point :))
		return szExt1;
		//return _T("prj");
	case 1:
		return szExt2;
	}
	return _T("");
}

const TCHAR * AsciiExp::LongDesc()
{
	return GetString(IDS_LONGDESC);
}

const TCHAR * AsciiExp::ShortDesc()
{
	return GetString(IDS_SHORTDESC);
}

const TCHAR * AsciiExp::AuthorName() 
{
	return GetString(IDS_AUTHORNAME);
}

const TCHAR * AsciiExp::CopyrightMessage() 
{
	return GetString(IDS_COPYRIGHT);
}

const TCHAR * AsciiExp::OtherMessage1() 
{
	return _T("");
}

const TCHAR * AsciiExp::OtherMessage2() 
{
	return _T("");
}

unsigned int AsciiExp::Version()
{
	return _ttoi(GetString(IDS_VERSION));
}

static INT_PTR CALLBACK AboutBoxDlgProc(HWND hWnd, UINT msg, 
	WPARAM wParam, LPARAM lParam)
{
	switch (msg) {
	case WM_INITDIALOG:
		CenterWindow(hWnd, GetParent(hWnd)); 
		break;
	case WM_COMMAND:
		switch (LOWORD(wParam)) {
		case IDOK:
			EndDialog(hWnd, 1);
			break;
		}
		break;
	case WM_SYSCOMMAND:
		switch (wParam) {
		case SC_CLOSE:
			EndDialog(hWnd, 0);
			break;
		default:
			break;
		}
		break;
	default:
		return FALSE;
	}
	return TRUE;
}       

void AsciiExp::ShowAbout(HWND hWnd)
{
	DialogBoxParam(hInstance, MAKEINTRESOURCE(IDD_ABOUTBOX), hWnd, AboutBoxDlgProc, 0);
}


// Dialog proc
static INT_PTR CALLBACK ExportDlgProc(HWND hWnd, UINT msg,
	WPARAM wParam, LPARAM lParam)
{
	Interval animRange;
	ISpinnerControl  *spin;

	AsciiExp *exp = DLGetWindowLongPtr<AsciiExp*>(hWnd); 
	switch (msg) {
	case WM_INITDIALOG:
		exp = (AsciiExp*)lParam;
		DLSetWindowLongPtr(hWnd, lParam); 
		CenterWindow(hWnd, GetParent(hWnd)); 
		CheckDlgButton(hWnd, IDC_MESHDATA, exp->GetIncludeMesh()); 
		CheckDlgButton(hWnd, IDC_ANIMKEYS, exp->GetIncludeAnim()); 
		CheckDlgButton(hWnd, IDC_MATERIAL, exp->GetIncludeMtl());
		CheckDlgButton(hWnd, IDC_MESHANIM, exp->GetIncludeMeshAnim()); 
		CheckDlgButton(hWnd, IDC_CAMLIGHTANIM, exp->GetIncludeCamLightAnim()); 
#ifndef DESIGN_VER
		CheckDlgButton(hWnd, IDC_IKJOINTS, exp->GetIncludeIKJoints()); 
#endif // !DESIGN_VER
		CheckDlgButton(hWnd, IDC_NORMALS,  exp->GetIncludeNormals()); 
		CheckDlgButton(hWnd, IDC_TEXCOORDS,exp->GetIncludeTextureCoords()); 
		CheckDlgButton(hWnd, IDC_VERTEXCOLORS,exp->GetIncludeVertexColors()); 
		CheckDlgButton(hWnd, IDC_OBJ_GEOM,exp->GetIncludeObjGeom()); 
		CheckDlgButton(hWnd, IDC_OBJ_SHAPE,exp->GetIncludeObjShape()); 
		CheckDlgButton(hWnd, IDC_OBJ_CAMERA,exp->GetIncludeObjCamera()); 
		CheckDlgButton(hWnd, IDC_OBJ_LIGHT,exp->GetIncludeObjLight()); 
		CheckDlgButton(hWnd, IDC_OBJ_HELPER,exp->GetIncludeObjHelper());

		CheckRadioButton(hWnd, IDC_RADIO_USEKEYS, IDC_RADIO_SAMPLE, 
			exp->GetAlwaysSample() ? IDC_RADIO_SAMPLE : IDC_RADIO_USEKEYS);
		
		// Setup the spinner controls for the controller key sample rate 
		spin = GetISpinner(GetDlgItem(hWnd, IDC_CONT_STEP_SPIN)); 
		spin->LinkToEdit(GetDlgItem(hWnd,IDC_CONT_STEP), EDITTYPE_INT ); 
		spin->SetLimits(1, 100, TRUE); 
		spin->SetScale(1.0f);
		spin->SetValue(exp->GetKeyFrameStep() ,FALSE);
		ReleaseISpinner(spin);
		
		// Setup the spinner controls for the mesh definition sample rate 
		spin = GetISpinner(GetDlgItem(hWnd, IDC_MESH_STEP_SPIN)); 
		spin->LinkToEdit(GetDlgItem(hWnd,IDC_MESH_STEP), EDITTYPE_INT ); 
		spin->SetLimits(1, 100, TRUE); 
		spin->SetScale(1.0f);
		spin->SetValue(exp->GetMeshFrameStep() ,FALSE);
		ReleaseISpinner(spin);

		// Setup the spinner controls for the floating point precision 
		spin = GetISpinner(GetDlgItem(hWnd, IDC_PREC_SPIN)); 
		spin->LinkToEdit(GetDlgItem(hWnd,IDC_PREC), EDITTYPE_INT ); 
		spin->SetLimits(1, 10, TRUE); 
		spin->SetScale(1.0f);
		spin->SetValue(exp->GetPrecision() ,FALSE);
		ReleaseISpinner(spin);

		// Setup the spinner control for the static frame#
		// We take the frame 0 as the default value
		animRange = exp->GetInterface()->GetAnimRange();
		spin = GetISpinner(GetDlgItem(hWnd, IDC_STATIC_FRAME_SPIN)); 
		spin->LinkToEdit(GetDlgItem(hWnd,IDC_STATIC_FRAME), EDITTYPE_INT ); 
		spin->SetLimits(animRange.Start() / GetTicksPerFrame(), animRange.End() / GetTicksPerFrame(), TRUE); 
		spin->SetScale(1.0f);
		spin->SetValue(0, FALSE);
		ReleaseISpinner(spin);

		// Enable / disable mesh options
		EnableWindow(GetDlgItem(hWnd, IDC_NORMALS), exp->GetIncludeMesh());
		EnableWindow(GetDlgItem(hWnd, IDC_TEXCOORDS), exp->GetIncludeMesh());
		EnableWindow(GetDlgItem(hWnd, IDC_VERTEXCOLORS), exp->GetIncludeMesh());
		break;

	case CC_SPINNER_CHANGE:
		spin = (ISpinnerControl*)lParam; 
		break;

	case WM_COMMAND:
		switch (LOWORD(wParam)) {
		case IDC_MESHDATA:
			// Enable / disable mesh options
			EnableWindow(GetDlgItem(hWnd, IDC_NORMALS), IsDlgButtonChecked(hWnd,
				IDC_MESHDATA));
			EnableWindow(GetDlgItem(hWnd, IDC_TEXCOORDS), IsDlgButtonChecked(hWnd,
				IDC_MESHDATA));
			EnableWindow(GetDlgItem(hWnd, IDC_VERTEXCOLORS), IsDlgButtonChecked(hWnd,
				IDC_MESHDATA));
			break;
		case IDOK:
			exp->SetIncludeMesh(IsDlgButtonChecked(hWnd, IDC_MESHDATA)); 
			exp->SetIncludeAnim(IsDlgButtonChecked(hWnd, IDC_ANIMKEYS)); 
			exp->SetIncludeMtl(IsDlgButtonChecked(hWnd, IDC_MATERIAL)); 
			exp->SetIncludeMeshAnim(IsDlgButtonChecked(hWnd, IDC_MESHANIM)); 
			exp->SetIncludeCamLightAnim(IsDlgButtonChecked(hWnd, IDC_CAMLIGHTANIM)); 
#ifndef DESIGN_VER
			exp->SetIncludeIKJoints(IsDlgButtonChecked(hWnd, IDC_IKJOINTS)); 
#endif // !DESIGN_VER
			exp->SetIncludeNormals(IsDlgButtonChecked(hWnd, IDC_NORMALS));
			exp->SetIncludeTextureCoords(IsDlgButtonChecked(hWnd, IDC_TEXCOORDS)); 
			exp->SetIncludeVertexColors(IsDlgButtonChecked(hWnd, IDC_VERTEXCOLORS)); 
			exp->SetIncludeObjGeom(IsDlgButtonChecked(hWnd, IDC_OBJ_GEOM)); 
			exp->SetIncludeObjShape(IsDlgButtonChecked(hWnd, IDC_OBJ_SHAPE)); 
			exp->SetIncludeObjCamera(IsDlgButtonChecked(hWnd, IDC_OBJ_CAMERA)); 
			exp->SetIncludeObjLight(IsDlgButtonChecked(hWnd, IDC_OBJ_LIGHT)); 
			exp->SetIncludeObjHelper(IsDlgButtonChecked(hWnd, IDC_OBJ_HELPER));
			exp->SetAlwaysSample(IsDlgButtonChecked(hWnd, IDC_RADIO_SAMPLE));

			spin = GetISpinner(GetDlgItem(hWnd, IDC_CONT_STEP_SPIN)); 
			exp->SetKeyFrameStep(spin->GetIVal()); 
			ReleaseISpinner(spin);

			spin = GetISpinner(GetDlgItem(hWnd, IDC_MESH_STEP_SPIN)); 
			exp->SetMeshFrameStep(spin->GetIVal());
			ReleaseISpinner(spin);

			spin = GetISpinner(GetDlgItem(hWnd, IDC_PREC_SPIN)); 
			exp->SetPrecision(spin->GetIVal());
			ReleaseISpinner(spin);
		
			spin = GetISpinner(GetDlgItem(hWnd, IDC_STATIC_FRAME_SPIN)); 
			exp->SetStaticFrame(spin->GetIVal() * GetTicksPerFrame());
			ReleaseISpinner(spin);
			
			EndDialog(hWnd, 1);
			break;
		case IDCANCEL:
			EndDialog(hWnd, 0);
			break;
		}
		break;
		case WM_SYSCOMMAND:
			switch (wParam) {
			case SC_CONTEXTHELP:
				exp->ShowAbout(hWnd);
				break;
				//return TRUE;
			case SC_CLOSE:
				EndDialog(hWnd, 0);
				break;
			default:
				break;
			}
			break;
		default:
			return FALSE;
	}
	return TRUE;
}       

// Dummy function for progress bar
DWORD WINAPI fn(LPVOID arg)
{
	return(0);
}


/************************************************************************/
/**** findVertIndex search for vertex in list of vertex per material ****/
/**** with same normal                                               ****/
/************************************************************************/
int findVertIndex ( t_VERTEX* currVert, int numVert, int vertIndex, Point3 vn, 
				   Point3 UV, FILE *pStream )  
{
    int i;
    float thr = 0.001;
    for( i = 0; i < numVert; i++ )
    {
        if( currVert[i].mainVertPointer == vertIndex ) 
		{ 
             // compare normals and texture
             if ( (fabs( vn.x - currVert[i].nx ) < thr) &&  
                  (fabs( vn.y - currVert[i].ny ) < thr) &&
                  (fabs( vn.z - currVert[i].nz ) < thr) &&
                  (fabs( UV.x - currVert[i].u ) < thr) &&
                  (fabs( UV.y - currVert[i].v ) < thr) )  
                  return( i ) ;                        
		}
    }          
     return (-1);
}     

/**********************************************************************/   
/**********************************************************************/
/**************** MAIN FUNCTION FOR EXPORTING A NODE ******************/
/**********************************************************************/
/**********************************************************************/
void AsciiExp::ExportNode( INode *currNode, char *LOD_string )
{
	 
	int i, j, numVertTot, numFacesTot, oldVertIndex;
	int currMat, currVert, currFace;
	
	// MAX NUMBER OF MATERIALS ALLOWED: 150
    int numVertMat[150] = {0}, numFacesMat[150] = {0};
    
    char materialName[150][50];
    char verticesString[80], textureString[80], facesString[80], 
	faceGroupsString[80], materialsString[80];    

    const int maxNumMat = 150;
    
	int faceIndex[150] = {0};
    int vertIndex[150] = {0};   

    int numVertsMat[150] = {0};
    int NumFacesMat[150] = {0};   
    
    t_FACE *(newFace[150]);  
    
    t_VERTEX *(newVertex[150]);
	t_VERTEX *vertex;

	int UVIndex;
	Point3 UV ;
	Point3 pt;
    int NumSubMaterials;
	MultiMtl* multimtl;

#ifdef DEBUG
       fprintf_UtoA( pStream, "Exporting node %s\n", FixupName((TCHAR*)currNode->GetName()) );
	   fflush( pStream );
#endif     

	sprintf( materialsString, "[%sMaterials]",LOD_string );
	sprintf( faceGroupsString, "[%sFaceGroups]",LOD_string );
	sprintf( verticesString, "[%sVertices_Frame0]",LOD_string  );
	sprintf( textureString, "[%sMaterialMapping]",LOD_string  );
	sprintf( facesString, "[%sFaces]",LOD_string );

    //Initialize materialName to blank to avoid problems
    for( i = 0; i < maxNumMat; i++ )
    {
         sprintf( materialName[i], " " );
    }         
     
   /************ get mesh pointer **********/
   TimeValue t = GetStaticFrame();
   BOOL deleteit = FALSE;
   TriObject* tri = GetTriObjectFromNode(currNode, t, deleteit);
   if (!tri)
   {
	   fprintf( pStream, "Not a Node that can be converted in a triangle mesh" );
	   return; //Not a Node that can be converted in a triangle mesh.
   }
   Mesh* mesh = &tri->GetMesh();

#ifdef DEBUG
   	fprintf(pStream, "Num Verts: %d\n", mesh->getNumVerts());
    fprintf(pStream, "Num Faces %d\n", mesh->getNumFaces());
	fflush( pStream );
#endif
   
   // Build mesh normals
   mesh->buildNormals();
   
	// Allocate memory for vertices
   numVertTot = mesh->getNumVerts();

   vertex = (t_VERTEX *)malloc( numVertTot*sizeof(t_VERTEX) );
   if( vertex == NULL )
   {
            fprintf( pStream, "malloc error- vertex! \n");   
            return;
    }    

   /***************************************/
   /************ read vertices ************/
   /***************************************/ 
   
   	// get node transformation matrix
    Matrix3 ObjTM = currNode->GetObjTMAfterWSM(t);
	BOOL negScale = TMNegParity(ObjTM);

	Matrix3 NodeTM;
	Matrix3 normTM;
	Point3 v, v1, vn1;
	Point3 row;

	if( strlen(LOD_string) == 0 && dmgLevel <= 0)	 // D0_00
	{	
		// store the TM of main object to be used with all LODs
#ifdef DEBUG_LOD
		fprintf( logFile, "Main TM stored.\n" );
		fflush(logFile);
#endif
		MainTM = currNode->GetNodeTM(GetStaticFrame());
		NodeTM = MainTM;
	}
	else
	{	
		// use the TM of main object
		NodeTM = MainTM;
	}
	/************************ PRINT HIER.HIM INFORMATION *********************/
	if( dmgLevel > 0 && strlen(LOD_string) == 0 )
		fprintf( hierFile,"Hidden\n");

	float thr = 0.0001;
	if( strlen(LOD_string) == 0 )
	{
		fprintf( hierFile,"Attaching \t");
		if( dmgLevel <= 0 )
		{	
			//child A and parent B
			//tmX = inverted tmB * tmA

			INode *parentNode = currNode->GetParentNode();
			Matrix3 parentNodeTM = parentNode->GetNodeTM(GetStaticFrame());		

			//Matrix3 newTM = (Inverse(parentNodeTM))*NodeTM;
			Matrix3 newTM = NodeTM*(Inverse(parentNodeTM));

			//row0
			row = newTM.GetRow(0);
			
			if( fabs(row.x) < thr )
				fprintf(hierFile, "0 ");
			else if (fabs(row.x - 1.) < thr )
					fprintf(hierFile, "1 ");
			else
					fprintf(hierFile, "%f ", row.x);

			if( fabs(row.y) < thr )
				fprintf(hierFile, "0 ");
			else if (fabs(row.y - 1.) < thr )
					fprintf(hierFile, "1 ");
			else
					fprintf(hierFile, "%f ", row.y);

			if( fabs(row.z) < thr )
				fprintf(hierFile, "0");
			else if (fabs(row.z - 1.) < thr )
					fprintf(hierFile, "1");
			else
					fprintf(hierFile, "%f", row.z);

			fprintf(hierFile, " \t");

		
			//row1
			row = newTM.GetRow(1);

			if( fabs(row.x) < thr )
				fprintf(hierFile, "0 ");
			else if (fabs(row.x - 1.) < thr )
					fprintf(hierFile, "1 ");
			else
					fprintf(hierFile, "%f ", row.x);

			if( fabs(row.y) < thr )
				fprintf(hierFile, "0 ");
			else if (fabs(row.y - 1.) < thr )
					fprintf(hierFile, "1 ");
			else
					fprintf(hierFile, "%f ", row.y);

			if( fabs(row.z) < thr )
				fprintf(hierFile, "0");
			else if (fabs(row.z - 1.) < thr )
					fprintf(hierFile, "1");
			else
					fprintf(hierFile, "%f", row.z);

			fprintf(hierFile, " \t");

			//row2
			row = newTM.GetRow(2);

				if( fabs(row.x) < thr )
				fprintf(hierFile, "0 ");
			else if (fabs(row.x - 1.) < thr )
					fprintf(hierFile, "1 ");
			else
					fprintf(hierFile, "%f ", row.x);

			if( fabs(row.y) < thr )
				fprintf(hierFile, "0 ");
			else if (fabs(row.y - 1.) < thr )
					fprintf(hierFile, "1 ");
			else
					fprintf(hierFile, "%f ", row.y);

			if( fabs(row.z) < thr )
				fprintf(hierFile, "0");
			else if (fabs(row.z - 1.) < thr )
					fprintf(hierFile, "1");
			else
					fprintf(hierFile, "%f", row.z);

			//row3
			//row = newTM.GetRow(3);
			//fprintf(hierFile, "%f %f %f \n", row.x*scaleFact, row.y*scaleFact, row.z*scaleFact );

			fprintf(hierFile, " \t\t");

			row = newTM.GetRow(3);

			fprintf_UtoA(hierFile,"%s\n", Format(row*scaleFact));
		}
		else
		{
			// Damage level always attached to undamage with I matrix
			fprintf(hierFile, "1 0 0 \t0 1 0 \t0 0 1 \t\t0 0 0\n" );
		}
	}
	/************************ END HIER.HIM INFORMATION *********************/

	// Order of the vertices. Get 'em counter clockwise if the objects is
	// negatively scaled.
    int vx1, vx2, vx3;
	if (negScale) {
		vx1 = 2;
		vx2 = 1;
		vx3 = 0;
	}
	else {
		vx1 = 0;
		vx2 = 1;
		vx3 = 2;
	}
   /*******************************************/   
   /*************** read faces ****************/   
   /*******************************************/
   
   // TODO: +++ Added by SAS~Storebror +++
   // If Node has no material, export with stand-in material name
	BOOL isFakeMaterial = FALSE;
	
	//Get the material from node
   Mtl* nodemtl = currNode->GetMtl();
   if (!nodemtl)
   {
      //fprintf( pStream, "no material applied to node!\n" );
      //return; //No material applied          
	  isFakeMaterial = TRUE;
   }
 
   if ( !isFakeMaterial && nodemtl->IsMultiMtl() )
   {
		multimtl = static_cast<MultiMtl*>(nodemtl);
		NumSubMaterials = multimtl->NumSubs();
   }

   // get number of faces   
   numFacesTot = mesh->getNumFaces();
   
#ifdef DEBUG     
   fprintf(pStream, "Processing Faces \n" );
   fprintf(pStream, "Num Faces %d\n\n\n", numFacesTot);  
   fflush( pStream );
#endif   

    // things concerning normals
    Point3 fn;  // Face normal
	Point3 vn, vn0;  // Vertex normal
	int  vertId, ID;
	Face* f;
	Mtl* mtl;

	/**** compute number of faces for each material ****/   
	for (i=0; i< numFacesTot; i++) 
	{
#ifdef DEBUG     
		fprintf(pStream, "Processing Face %d/%d\n", i, numFacesTot );
		fflush( pStream );
#endif 
        //Get the ID in this face (apply the modulo)
		if (isFakeMaterial)
		{
			ID = 0;
			strcpy(materialName[ID], "<Manual Edit required: Set Material Name here!>");
		}
		else if ( nodemtl->IsMultiMtl() )
		{
			ID = mesh->getFaceMtlIndex(i) % NumSubMaterials;
	        mtl = multimtl->GetSubMtl(ID);
			strcpy( materialName[ID], FixupNameAnsi((char*)mtl->GetName().ToCStr().data()));
		}
		else
		{
			ID = 0;
			strcpy( materialName[ID], FixupNameAnsi((char*)nodemtl->GetName().ToCStr().data()) ); 
        }
#ifdef DEBUG     
		fprintf(pStream, "ID = %d\n", ID );
		fflush( pStream );
#endif 

		numFacesMat[ID] ++;
		numVertsMat[ID] += 3; // not optimized
	}
    /*************************************/ 
    /********** allocate memory **********/   
    /*************************************/ 
    for( i = 0; i < maxNumMat; i++ )
    {
        //fprintf( pStream, "mat = %d     numfaces = %d   numverts = %d\n", i, numFacesMat[i], numVertsMat[i] );
        newFace[i] = (t_FACE *)(malloc( numFacesMat[i]*sizeof(t_FACE)) );
        if( newFace[i] == NULL )
        {
            fprintf( pStream, "malloc error - faces! \n");   
            return;
        }    
        newVertex[i] = (t_VERTEX *)(malloc( numVertsMat[i]*sizeof(t_VERTEX)) );
        if( newVertex[i] == NULL )
        {
            fprintf( pStream,"malloc error - materials! \n");   
            return;
        }    
    }    

    /********** MAIN LOOP on FACES ***********/   
   	for (i=0; i< numFacesTot; i++) 
    {
#ifdef DEBUG     
		fprintf(pStream, "\nProcessing Face %d\n", i );
		fflush( pStream );
#endif 

        //Get the ID in this face (apply the modulo)
		if (isFakeMaterial)
		{
			ID = 0;
		}
		else if ( nodemtl->IsMultiMtl() )
		{
			ID = mesh->getFaceMtlIndex(i) % NumSubMaterials;
			
			//Get the material applied on this face			
			//mtl = multimtl->GetSubMtl(ID);
		}
		else
			ID = 0;
        
/*********************** BEGIN PROCESSING **************************/
		
		// get face pointer;
		f = &mesh->faces[i]; 
		
		//set material name and ID
        //strcpy( currMatName, mtl->GetName() ); 
        currMat = ID;               

         /**** VERTEX V1 ****/
        vertId = f->getVert(vx1);        

		v1 = ObjTM * mesh->verts[vertId];
		v = v1 * Inverse(NodeTM);

		// transformation for normals
   	    vn1 = GetVertexNormal(mesh, i, mesh->getRVertPtr(vertId));   
		normTM = NodeTM;	
		pt.Set(0.,0.,0.);
		normTM.SetRow(3, pt);
		vn = vn1 * Inverse(normTM);
		//vn = vn1.Normalize();

        TVFace& _tvface = mesh->tvFace[i];
        UVIndex = _tvface.t[vx1];
        UV = mesh->tVerts[UVIndex];
        
        /* check if the vertex is already listed in that material */
        /* with same normal and texture */
        oldVertIndex = findVertIndex( newVertex[currMat], vertIndex[currMat], vertId, vn, UV, pStream );
        if( oldVertIndex == -1 ) //not found
        {
#ifdef DEBUG  
              fprintf( pStream, "V1 Not found: %d %d \n", vertId, vertIndex[currMat] ); 
   	  	      fflush( pStream );
#endif 
             /* insert new vertex ***/	
			 newVertex[currMat][vertIndex[currMat]].mainVertPointer = vertId;    
			 newVertex[currMat][vertIndex[currMat]].x = v.x * scaleFact;
             newVertex[currMat][vertIndex[currMat]].y = v.y * scaleFact;		 
             newVertex[currMat][vertIndex[currMat]].z = v.z * scaleFact;
             newVertex[currMat][vertIndex[currMat]].nx = vn.x;
             newVertex[currMat][vertIndex[currMat]].ny = vn.y;
             newVertex[currMat][vertIndex[currMat]].nz = vn.z;   
             newVertex[currMat][vertIndex[currMat]].u = UV.x;
             newVertex[currMat][vertIndex[currMat]].v = UV.y;
             newFace[currMat][faceIndex[currMat]].v1 = vertIndex[currMat];

#ifdef DEBUG  
              fprintf( pStream, "vertIndex[currMat] : %d \n", vertIndex[currMat] ); 
			  fflush( pStream );
#endif 
			  vertIndex[currMat]++;                                                                
         }    
         else
         {
             /* point to existing vertex ***/
             newFace[currMat][ faceIndex[currMat] ].v1  = oldVertIndex;
#ifdef DEBUG  
             fprintf( pStream, "V1 found: %d %d \n",vertId, vertIndex[currMat] ); 
			 fflush( pStream );
#endif 
         }    

        /**** VERTEX V2 ****/
        vertId = f->getVert(vx2);

        v1 = ObjTM * mesh->verts[vertId];
		v = v1 * Inverse(NodeTM);

		// transformation for normals
   	    vn1 = GetVertexNormal(mesh, i, mesh->getRVertPtr(vertId));   
		normTM = NodeTM;		
		normTM.SetRow(3, pt);
		vn = vn1 * Inverse(normTM);
		//vn1 = vn.Normalize();

        _tvface = mesh->tvFace[i];
        UVIndex = _tvface.t[vx2];
        UV = mesh->tVerts[UVIndex];
        
        /* check if the vertex is already listed in that material */
        /* with same normal and texture */
        oldVertIndex = findVertIndex( newVertex[currMat], vertIndex[currMat], vertId, vn, UV, pStream );
        if( oldVertIndex == -1 ) //not found
        {
#ifdef DEBUG  
              fprintf( pStream, "V2 Not found: %d %d \n", vertId, vertIndex[currMat] );
			  fflush( pStream );
#endif 
             /*** insert new vertex ***/     
             newVertex[currMat][vertIndex[currMat]].mainVertPointer = vertId;    
			 newVertex[currMat][vertIndex[currMat]].x = v.x * scaleFact;
             newVertex[currMat][vertIndex[currMat]].y = v.y * scaleFact;		 
             newVertex[currMat][vertIndex[currMat]].z = v.z * scaleFact;
             newVertex[currMat][vertIndex[currMat]].nx = vn.x;
             newVertex[currMat][vertIndex[currMat]].ny = vn.y;
             newVertex[currMat][vertIndex[currMat]].nz = vn.z;             
             newVertex[currMat][vertIndex[currMat]].u = UV.x;
             newVertex[currMat][vertIndex[currMat]].v = UV.y;
             newFace[currMat][faceIndex[currMat]].v2 = vertIndex[currMat];

#ifdef DEBUG  
              fprintf( pStream, "vertIndex[currMat] : %d \n", vertIndex[currMat] ); 
			  fflush( pStream );
#endif 
			  vertIndex[currMat]++;                                                            

         }    
         else
         {
             /*** point to existing vertex ***/
             newFace[currMat][ faceIndex[currMat] ].v2  = oldVertIndex;

#ifdef DEBUG  
             fprintf( pStream, "V2 found: %d %d \n",vertId, vertIndex[currMat] ); 
  	 	     fflush( pStream );
#endif 
         }    

         /**** VERTEX V3 ****/
        vertId = f->getVert(vx3); 
        
		v1 = ObjTM * mesh->verts[vertId];
		v = v1 * Inverse(NodeTM);

		// transformation for normals
   	    vn1 = GetVertexNormal(mesh, i, mesh->getRVertPtr(vertId));   
		normTM = NodeTM;		
		normTM.SetRow(3, pt);
		vn = vn1 * Inverse(normTM);
		//vn = vn1.Normalize();

        _tvface = mesh->tvFace[i];
        UVIndex = _tvface.t[vx3];
        UV = mesh->tVerts[UVIndex];
        
        /* check if the vertex is already listed in that material */
        /* with same normal and texture */
        oldVertIndex = findVertIndex( newVertex[currMat], vertIndex[currMat], vertId, vn, UV, pStream );
        if( oldVertIndex == -1 ) //not found
        {
#ifdef DEBUG  
              fprintf( pStream, "V3 Not found: %d %d \n", vertId, vertIndex[currMat] ); 
			  fflush( pStream );
#endif 
             /*** insert new vertex ***/
             newVertex[currMat][vertIndex[currMat]].mainVertPointer = vertId;    
			 newVertex[currMat][vertIndex[currMat]].x = v.x  * scaleFact;
             newVertex[currMat][vertIndex[currMat]].y = v.y * scaleFact;		 
             newVertex[currMat][vertIndex[currMat]].z = v.z * scaleFact;
             newVertex[currMat][vertIndex[currMat]].nx = vn.x;
             newVertex[currMat][vertIndex[currMat]].ny = vn.y;
             newVertex[currMat][vertIndex[currMat]].nz = vn.z;             
             newVertex[currMat][vertIndex[currMat]].u = UV.x;
             newVertex[currMat][vertIndex[currMat]].v = UV.y;
             newFace[currMat][faceIndex[currMat]].v3 = vertIndex[currMat];

#ifdef DEBUG  
              fprintf( pStream, "vertIndex[currMat] : %d \n", vertIndex[currMat] ); 
			  fflush( pStream );
#endif 
			  vertIndex[currMat]++;      
         }    
         else
         {
             /*** point to existing vertex ***/
             newFace[currMat][ faceIndex[currMat] ].v3  = oldVertIndex;
#ifdef DEBUG  
             fprintf( pStream, "V3 found: %d %d \n",vertId, vertIndex[currMat] ); 
			 fflush( pStream );
#endif 
         }  
                     
        faceIndex[currMat]++;        
    }// end main loop faces
    
	/******************************************************/
	/******************* print results ********************/
	/******************************************************/

	/*** print Materials SKIPPING THE NULL MATERIAL ***/
    fprintf( pStream, "\n%s\n", materialsString  );   
    for( i = 0; i < maxNumMat; i++ )
    {
         if( vertIndex[i] > 0 && ( strcmp( "NULL", materialName[i] ) != 0 ) )  
             fprintf( pStream, "%s\n", materialName[i] );   
    }        
    
   /*** print Facegroups ***/
   fprintf( pStream, "\n%s\n", faceGroupsString );
   
   numVertTot = 0;
   for( i = 0; i < maxNumMat; i++ ) 
        if( vertIndex[i] > 0 && ( strcmp( "NULL", materialName[i] ) != 0 ) ) 
                numVertTot += vertIndex[i];
 
   numFacesTot = 0;
   for( i = 0; i < maxNumMat; i++ ) 
        if( vertIndex[i] > 0 && ( strcmp( "NULL", materialName[i] ) != 0 ) ) 
                numFacesTot += faceIndex[i];
  
   fprintf( pStream, "%d %d\n", numVertTot, numFacesTot );

   currVert = 0;
   currFace = 0;
   currMat = 0;

   for( i = 0; i < maxNumMat; i++ )
   {    
        if( vertIndex[i] > 0 && ( strcmp( "NULL", materialName[i] ) != 0 ) )  
        {      
               fprintf( pStream, "%d %d %d %d %d 0\n", currMat, currVert, vertIndex[i], currFace, faceIndex[i] );
               currVert += vertIndex[i];
               currFace += faceIndex[i];  
               currMat++;
        }
   } 
   
   /*** print vertices ***/
   fprintf( pStream, "\n%s\n", verticesString  );

    for( i = 0; i < maxNumMat; i++ )
    {
         if ( strcmp( "NULL", materialName[i] ) != 0 )
         {     
             for( j = 0; j < vertIndex[i]; j++ )
             {
                 fprintf( pStream, "%f %f %f %f %f %f\n", 
                 newVertex[i][j].x, newVertex[i][j].y, newVertex[i][j].z, 
                 newVertex[i][j].nx, newVertex[i][j].ny, newVertex[i][j].nz );
             }
         }     
    }        
   
   /*** print UV map ***/
   fprintf( pStream, "\n%s\n", textureString  );
   for( i = 0; i < maxNumMat; i++ )
   {
         if ( strcmp( "NULL", materialName[i] ) != 0 )
         {          
             for( j = 0; j < vertIndex[i]; j++ )
             {	
				  // modify the Y
                  fprintf( pStream, "%f %f \n", 
                       newVertex[i][j].u, 1 - newVertex[i][j].v);
             }  
         }       
   }        
   
   /*** print faces ***/
   fprintf( pStream, "\n%s\n", facesString  );
   for( i = 0; i < maxNumMat; i++ )
   {
         if ( strcmp( "NULL", materialName[i] ) != 0 )
         {
             for( j = 0; j < faceIndex[i]; j++ )
             {
                  fprintf( pStream, "%d %d %d\n", 
                       newFace[i][j].v1, newFace[i][j].v2, newFace[i][j].v3);
             }     
         }
   }   
   fflush( pStream );

    // FREE MEMORY
    free( vertex );
    for( i = 0; i < maxNumMat; i++ )
    {
        if ( newFace[i] != NULL ) free( newFace[i] );
		if ( newVertex[i] != NULL ) free( newVertex[i] );
    }    

   //If necessary, delete the TriObject and all references to this object.
   if (deleteit)
   tri->DeleteMe() ;  
    
#ifdef DEBUG        
 	fprintf(pStream,"END PROCESSING\n" );	
#endif 	
}
/***************************************************************/
/***************************************************************/
/************* MAIN FUNCTION FOR EXPORTING A HOOK **************/
/***************************************************************/
/***************************************************************/
//void AsciiExp::ExportHook( INode *currNode, INode *parentNode )
void AsciiExp::ExportHook( INode *currNode, INode *parentNode, char* nodeName )
{			
	//int numVertTot;

	/************ get mesh pointer **********/
    TimeValue t = GetStaticFrame();
    BOOL deleteit = FALSE;
    TriObject* tri = GetTriObjectFromNode(currNode, t, deleteit);
    if (!tri)
    {
		DummyObject* dum = GetDummyObjectFromNode(currNode, t, deleteit);
		if (!dum) {
	   		fprintf( pStream, "Not a Node that can be converted in a triangle mesh or dummy helper object" );
	   		return; //Not a Node that can be converted in a triangle mesh.
		}
   	}

//    Mesh* mesh = &tri->GetMesh();
//	numVertTot = mesh->getNumVerts();

#ifdef DEBUG
     fprintf_UtoA( pStream, "Exporting hook object %s\n", FixupName((TCHAR*)currNode->GetName() ) );
	 fflush( pStream );
#endif     
   
   	// get transformation matrices of parent node
	Matrix3 NodeTM = currNode->GetNodeTM(GetStaticFrame()) ;
	Matrix3 parentNodeTM = parentNode->GetNodeTM(GetStaticFrame());			

   	// get transformation matrix of hook
	//Matrix3 hookNodeTM = (Inverse(parentNodeTM))*NodeTM;
	Matrix3 hookNodeTM = NodeTM*(Inverse(parentNodeTM));

    Point3 v, v1, row; 
	float thr = 0.0001;

/*************************************************
for( i = 0; i < numVertTot; i++ )
    {
		v1 = parentObjTM * mesh->verts[i];
		v = v1 * Inverse(parentNodeTM);              

		 fprintf( pStream, "%f %f %f \n", 
			 scaleFact * v.x, 
			 scaleFact * v.y, 
			 scaleFact * v.z );  
    }
	fprintf( pStream, "\n");
**************************************************/
	//row0
	row = hookNodeTM.GetRow(0);

	if( fabs(row.x) < thr )
		fprintf(pStream, "0 ");
	else if (fabs(row.x - 1.) < thr )
			fprintf(pStream, "1 ");
	else
			fprintf(pStream, "%f ", row.x);

	if( fabs(row.y) < thr )
		fprintf(pStream, "0 ");
	else if (fabs(row.y - 1.) < thr )
			fprintf(pStream, "1 ");
	else
			fprintf(pStream, "%f ", row.y);

	if( fabs(row.z) < thr )
		fprintf(pStream, "0");
	else if (fabs(row.z - 1.) < thr )
			fprintf(pStream, "1");
	else
			fprintf(pStream, "%f", row.z);

	fprintf(pStream, " \t");

	//row1
	row = hookNodeTM.GetRow(1);

	if( fabs(row.x) < thr )
		fprintf(pStream, "0 ");
	else if (fabs(row.x - 1.) < thr )
			fprintf(pStream, "1 ");
	else
			fprintf(pStream, "%f ", row.x);

	if( fabs(row.y) < thr )
		fprintf(pStream, "0 ");
	else if (fabs(row.y - 1.) < thr )
			fprintf(pStream, "1 ");
	else
			fprintf(pStream, "%f ", row.y);

	if( fabs(row.z) < thr )
		fprintf(pStream, "0");
	else if (fabs(row.z - 1.) < thr )
			fprintf(pStream, "1");
	else
			fprintf(pStream, "%f", row.z);

	fprintf(pStream, " \t");

	//row2
	row = hookNodeTM.GetRow(2);

		if( fabs(row.x) < thr )
		fprintf(pStream, "0 ");
	else if (fabs(row.x - 1.) < thr )
			fprintf(pStream, "1 ");
	else
			fprintf(pStream, "%f ", row.x);

	if( fabs(row.y) < thr )
		fprintf(pStream, "0 ");
	else if (fabs(row.y - 1.) < thr )
			fprintf(pStream, "1 ");
	else
			fprintf(pStream, "%f ", row.y);

	if( fabs(row.z) < thr )
		fprintf(pStream, "0");
	else if (fabs(row.z - 1.) < thr )
			fprintf(pStream, "1");
	else
			fprintf(pStream, "%f", row.z);

/************************************************
	row = hookNodeTM.GetRow(0);
	fprintf_UtoA(pStream,"%s ", Format(row));

	row = hookNodeTM.GetRow(1);
	fprintf_UtoA(pStream,"%s ", Format(row));

	row = hookNodeTM.GetRow(2);
	fprintf_UtoA(pStream,"%s ", Format(row));	
*************************************************/

	fprintf(pStream, " \t\t");

	row = hookNodeTM.GetRow(3);

	fprintf_UtoA(pStream,"%s", Format(row*scaleFact));
	fprintf(pStream,"\t// %s\n", nodeName);
	//fprintf(pStream,"%s \n", Format(row*scaleFact));

}

/***************************************************************/
/***************************************************************/
/******** MAIN FUNCTION FOR EXPORTING A SHADOW OBJECT *********/
/***************************************************************/
/***************************************************************/
void AsciiExp::ExportShadowNode( INode *currNode, char *LODstring )
{	 
	int i, numVertTot, numFacesTot;
	int vert1Id, vert2Id, vert3Id;
		
	/************ get mesh pointer **********/
    TimeValue t = GetStaticFrame();
    BOOL deleteit = FALSE;
    TriObject* tri = GetTriObjectFromNode(currNode, t, deleteit);
    if (!tri)
    {
	   		fprintf( pStream, "Not a Node that can be converted in a triangle mesh" );
	   		return; //Not a Node that can be converted in a triangle mesh.
   	}

    Mesh* mesh = &tri->GetMesh();
		
#ifdef DEBUG
     fprintf_UtoA( pStream, "Exporting shadow object %s\n", FixupName((TCHAR*)currNode->GetName() ) );
	 fflush( pStream );
#endif     
   
	// Allocate memory for vertices
   numVertTot = mesh->getNumVerts();

#ifdef DEBUG_COLL  
	fprintf( pStream, "number of vertices = %d\n", numVertTot );
#endif

	// print heading information
	fprintf( pStream, "\n[%sShVertices_Frame0]\n", LODstring );

   /*******************************************/
   /************** read vertices **************/
   /*******************************************/ 
   
   	// get node transformation matrix
   	Matrix3 ObjTM = currNode->GetObjTMAfterWSM(t);
	BOOL negScale = TMNegParity(ObjTM);

	//Matrix3 NodeTM = currNode->GetNodeTM(GetStaticFrame());

	// use TM of main node
	Matrix3 NodeTM = MainTM;

    Point3 v, v1; 

    for( i = 0; i < numVertTot; i++ )
    {
		v1 = ObjTM * mesh->verts[i];
		v = v1 * Inverse(NodeTM);              

		 fprintf( pStream, "%f %f %f \n", 
			 scaleFact * v.x, 
			 scaleFact * v.y, 
			 scaleFact * v.z );  
    }
    
	// Order of the vertices. Get 'em counter clockwise if the objects is
	// negatively scaled.
	// Not understood, but this should do no harm....
    int vx1, vx2, vx3;
	if (negScale) {
		vx1 = 2;
		vx2 = 1;
		vx3 = 0;
	}
	else {
		vx1 = 0;
		vx2 = 1;
		vx3 = 2;
	}
   /*******************************************/   
   /*************** read faces ****************/   
   /*******************************************/
   // get number of faces   
   numFacesTot = mesh->getNumFaces();

	fprintf( pStream, "\n[%sShFaces]\n", LODstring );
		
   	for (i=0; i< numFacesTot; i++) 
    {
#ifdef DEBUG     
		fprintf(pStream, "\nProcessing Face %d\n", i );
		fflush( pStream );
#endif 
                    
        vert1Id = mesh->faces[i].v[vx1];                
        vert2Id = mesh->faces[i].v[vx2];        
        vert3Id = mesh->faces[i].v[vx3];
        
        fprintf( pStream, "%d %d %d \n", vert1Id, vert2Id, vert3Id );                   
        
        fflush( pStream );    

    }// end main loop faces
    
   	fflush( pStream );

   //If necessary, delete the TriObject and all references to this object.
   if (deleteit)
   tri->DeleteMe() ;  
    
#ifdef DEBUG        
 	fprintf(pStream,"END PROCESSING\n" );	
#endif 	
}

/***************************************************************/
/***************************************************************/
/******* MAIN FUNCTION FOR EXPORTING A COLLISION OBJECT ********/
/***************************************************************/
/***************************************************************/
void AsciiExp::ExportCollNode( INode *currNode, int k )
{	 
	int i, numVertTot, numFacesTot;
	int vert1Id, vert2Id, vert3Id;
     
	int	*numNeigh;
		
	/************ get mesh pointer **********/
    TimeValue t = GetStaticFrame();
    BOOL deleteit = FALSE;
    TriObject* tri = GetTriObjectFromNode(currNode, t, deleteit);
    if (!tri)
    {
	   		fprintf( pStream, "Not a Node that can be converted in a triangle mesh" );
	   		return; //Not a Node that can be converted in a triangle mesh.
   	}

    Mesh* mesh = &tri->GetMesh();
		
#ifdef DEBUG
     fprintf_UtoA( pStream, "Exporting collision object %s\n", FixupName((TCHAR*)currNode->GetName() ) );
	 fflush( pStream );
#endif     
   
	// Allocate memory for vertices
   numVertTot = mesh->getNumVerts();

#ifdef DEBUG_COLL  
	fprintf( pStream, "number of vertices = %d\n", numVertTot );
#endif

   numNeigh = (int *)malloc( numVertTot*sizeof(int) );
   if( numNeigh == NULL )
   {
            fprintf( pStream, "malloc error- numNeigh! \n");   
            return;
   }    
	// print heading information
	fprintf( pStream, "\n[CoCommon_b0p%d]\n", k );
	fprintf( pStream, "Type Mesh\n" );		
	fprintf( pStream, "NFrames 1\n" );		
	fprintf_UtoA( pStream, "Name %s\n\n", FixupName((TCHAR*)currNode->GetName() ) );		
	
	fprintf( pStream, "[CoVer0_b0p%d]\n", k );
		

   /*******************************************/
   /************** read vertices **************/
   /*******************************************/ 
   
   	// get node transformation matrix
   	Matrix3 ObjTM = currNode->GetObjTMAfterWSM(t);
	Matrix3 NodeTM = currNode->GetNodeTM(GetStaticFrame());
	BOOL negScale = TMNegParity(ObjTM);


/******************************************************/
// USE MAIN TM
    NodeTM = MainTM;

	Point3 v, v1; 

    for( i = 0; i < numVertTot; i++ )
    {           
		v1 = ObjTM * mesh->verts[i];
		v = v1 * Inverse(NodeTM);      

		 fprintf( pStream, "%f %f %f \n", 
			 scaleFact * v.x, 
			 scaleFact * v.y, 
			 scaleFact * v.z );     
		}
    
    fprintf( pStream, "\n[CoNeiCnt_b0p%d]\n", k );
    
   /************************************************/   
   /*************** read neighbours ****************/   
   /************************************************/
   AdjEdgeList* mAdjEdges = new AdjEdgeList(*mesh);
   if (! mAdjEdges ) 
   {
   		fprintf( pStream, "Error with neighbours list\n" ); 
   		return;
   }

   //DWORDTab *edgePtr;
   //DWORDTab edgePtr;
   bool endList = false;
   int adj, edgeId;
   

   // find number of neighbours for each vertex	
   for ( i = 0; i < numVertTot; i++ )
   {
#ifdef DEBUG_COLL  
   		fprintf( pStream, "vertex %d\n", i ); 
		fflush( pStream );
#endif

     	numNeigh[i] = mAdjEdges->list[i].Count();
		fprintf( pStream, "%d\n", numNeigh[i] );
   	  
#ifdef DEBUG_COLL
	    fprintf( pStream, "number of Neighbours = %d\n\n", i, edgeId );	
#endif   
   }

   // print number of neighbours for each vertex	
   fprintf( pStream, "\n[CoNei_b0p%d]\n", k );

   for ( i = 0; i < numVertTot; i++ )
   {
 		for( edgeId = 0; edgeId < numNeigh[i]; edgeId++ )
   		{
			adj = mAdjEdges->list[i][edgeId];
			MEdge edge = mAdjEdges->edges[adj];
#ifdef DEBUG_COLL  
			fprintf( pStream, "Neighbour 0: %d\n", edge.v[0] );
			fprintf( pStream, "Neighbour 1: %d\n", edge.v[1] );
#endif     	
			if( edge.v[0] != i )
				fprintf( pStream, "%d\n", edge.v[0] );
			else
				fprintf( pStream, "%d\n", edge.v[1] );
   		}
	 }

	// Order of the vertices. Get 'em counter clockwise if the objects is
	// negatively scaled.
	// Not understood, but this should do no harm....
    int vx1, vx2, vx3;
	if (negScale) {
		vx1 = 2;
		vx2 = 1;
		vx3 = 0;
	}
	else {
		vx1 = 0;
		vx2 = 1;
		vx3 = 2;
	}
   /*******************************************/   
   /*************** read faces ****************/   
   /*******************************************/
   // get number of faces   
   numFacesTot = mesh->getNumFaces();

	fprintf( pStream, "\n[CoFac_b0p%d]\n", k );
		
   	for (i=0; i< numFacesTot; i++) 
    {
#ifdef DEBUG     
		fprintf(pStream, "\nProcessing Face %d\n", i );
		fflush( pStream );
#endif                     
        vert1Id = mesh->faces[i].v[vx1];                
        vert2Id = mesh->faces[i].v[vx2];        
        vert3Id = mesh->faces[i].v[vx3];
        
        fprintf( pStream, "%d %d %d \n", vert1Id, vert2Id, vert3Id );                   
        
        fflush( pStream );    

    }// end main loop faces

   	fflush( pStream );

    // FREE MEMORY
    free( numNeigh );

   //If necessary, delete the TriObject and all references to this object.
   if (deleteit)
		tri->DeleteMe() ;  
    
#ifdef DEBUG        
 	fprintf(pStream,"END PROCESSING\n" );	
#endif 	
}

     
/******************************** E X P O R T E R **************************************/
// Start the exporter!
// This is the real entrypoint to the exporter. After the user has selected
// the filename (and he's prompted for overwrite etc.) this method is called.
int AsciiExp::DoExport(const TCHAR *name,ExpInterface *ei,Interface *i, BOOL suppressPrompts, DWORD options) 
{
	// Set a global prompt display switch
	showPrompts = suppressPrompts ? FALSE : TRUE;
	exportSelected = (options & SCENE_EXPORT_SELECTED) ? TRUE : FALSE;
	char prjFileName[MAX_PATH], objName[80], objFullName[80], dummystr[80], dmgString[10], hierFileName[MAX_PATH];
	int k, j, numObjects, numDamage;
	FILE *prjFile = NULL;

	numErrors = 0;
	numWarnings = 0;

	// Grab the interface pointer.  
	ip = i;

	// Get the options the user selected the last time
	ReadConfig();

	if(showPrompts) 
	{
		// Prompt the user with our dialogbox, and get all the options.
		if (!DialogBoxParam(hInstance, MAKEINTRESOURCE(IDD_ASCIIEXPORT_DLG),
			ip->GetMAXHWnd(), ExportDlgProc, (LPARAM)this)) {
			return 1;
			}
	}

	char buf[16];
	sprintf(buf, "%%4.%df", nPrecision);
	LPCWSTR szBuf = AnsiToUnicode(buf);
	_tcscpy(szFmtStr, szBuf);
	delete(szBuf);

	// Open log file
    logFile = fopen( "IL2export.log", "w" );
    if( logFile == NULL )
    {
        return(0);
    }

	char* oldLocale = setlocale(LC_ALL, "");
	setlocale(LC_ALL, "en-US");
	setlocale(LC_NUMERIC, "C");
	Sleep(0);



	// Open project file
	LPSTR lpBuf = UnicodeToAnsi(name);

	if (EndsWith(lpBuf, ".cfg")) {
		// This is a single mesh export!
		dmgLevel = -1;
		scaleFact = 0.0F;
		char *ptr = strrchr(lpBuf, '\\');
		if (ptr != NULL)
			strncpy_s(objName, sizeof(objName), ptr + 1, strlen(lpBuf) - 5 - (ptr - lpBuf));
		else
			strncpy_s(objName, sizeof(objName), lpBuf, strlen(lpBuf) - 4);
		delete(lpBuf);
		fprintf(logFile, "**************************************** Exporting single Mesh %s\n", objName);
		szBuf = AnsiToUnicode(objName);

		DoNodeExport(szBuf, ip);
		delete(szBuf);
		fprintf(logFile, "**************************************** Done\n");
	}
	else {


		strcpy(prjFileName, lpBuf);
		delete(lpBuf);
		prjFile = fopen(prjFileName, "r");
		if (prjFile == NULL)
		{
			fprintf(logFile, "error opening project file %s \n", prjFileName);
			fflush(logFile);
			return 1;
		}


		fscanf(prjFile, "%s %d %c %f", dummystr, &numObjects, &dmgChar, &scaleFact);
		fprintf(logFile, " Read num objects = %d, dmgChar %c scale Factor %f\n", numObjects, dmgChar, scaleFact);

		// Open hier file in REWRITE mode
		sprintf(hierFileName, "hier.him");
		hierFile = fopen(hierFileName, "wt");
		if (hierFile == NULL)
		{
			fprintf(logFile, "****** ERROR: Error opening file %s\n", hierFileName);
			numErrors++;
			return 1;
		}
		fclose(hierFile);

		for (k = 0; k < numObjects; k++)
		{
			if (k > 1000) { fclose(logFile); return 1; }
			if (fscanf(prjFile, "%s %d", objName, &numDamage) < 2) continue;
			fprintf(logFile, "read objName = %s, numDmg = %d\n", objName, numDamage);

			if (numDamage == 0) // NO DAMAGE e.g. CAPs
			{
				dmgLevel = -1;
				fprintf(logFile, "**************************************** Exporting object %s\n", objName);
				szBuf = AnsiToUnicode(objName);
				DoNodeExport(szBuf, ip);
				delete(szBuf);
				fprintf(logFile, "**************************************** Done\n");
			}
			else
				for (j = 0; j < numDamage; j++)
				{
					dmgLevel = j;
					if (j > 10) { fclose(logFile); return 1; }
					sprintf(objFullName, "%s_%c%d", objName, dmgChar, dmgLevel);
					sprintf(dmgString, "_%c%d", dmgChar, j);
					fprintf(logFile, "**************************************** Exporting object %s D%d\n", objFullName, j);
					szBuf = AnsiToUnicode(objName);
					DoNodeExport(szBuf, ip);
					delete(szBuf);
					fprintf(logFile, "**************************************** Done\n");
				}
		}
	}

	fprintf( logFile, "%d errors, %d warnings\n", numErrors, numWarnings );
	fclose( logFile );
	if (prjFile != NULL) fclose( prjFile );
	if (pStream != NULL) {
		fflush(pStream);
		fclose(pStream);
	}
	if (hierFile != NULL) {
		fflush(hierFile);
		fclose(hierFile);
	}
	setlocale(LC_ALL, oldLocale);
	return (1);
} 

/********************* S I N G L E    N O D E      E X P O R T E R ********************/
int AsciiExp::DoNodeExport(const TCHAR *name ,Interface *i ) 
{
	// Set a global prompt display switch
	INode *currNode, *newNode, *parentNode;
	char nodeName[100], dummystr[100], shadowNodeName[100];
	char LODstring[8];
	char cfgFileName[MAX_PATH], hierFileName[MAX_PATH];
	int k;
	float dummyFloat;
	BOOL skipDamageSuffix = FALSE;

	// Grab the interface pointer.  
	ip = i;

	char buf[16];
	sprintf(buf, "%%4.%df", nPrecision);
	LPCWSTR szBuf = AnsiToUnicode(buf);
	_tcscpy(szFmtStr, szBuf);
	delete(szBuf);


	// get name of node to be exported
	//cut away the extension
/********************************************
	char *ptr;
	ptr = (char *)strchr( name, '.' ); 
    if ( ptr == NULL )
    {
       return(0);     
    }     
    ptr[0] = (char)NULL;
******************************************/

    char baseName[100];
	LPSTR lpBuf = UnicodeToAnsi(name);
	strcpy( baseName, lpBuf );
	delete(lpBuf);

#ifdef DEBUG_LOD
	fprintf( logFile, "Base name = %s\n", baseName );
	fflush( logFile );
#endif
	
	// Open hier file in APPEND mode
	//sprintf( hierFileName, "my_hier.him" );
	sprintf(hierFileName, "hier.him");
    hierFile = fopen( hierFileName, "a" );
    if( hierFile == NULL )
    {
				fprintf( logFile, "****** ERROR: Error opening file %s\n", hierFileName );
				numErrors++;
				return 1;
	}
	
	// ******* configuration file *******
	if( dmgLevel != -1 )
	{
		// part with damage levels
		sprintf( cfgFileName, "%s_%c%d.cfg", baseName, dmgChar, dmgLevel );
		if (!FileExists(cfgFileName) && dmgLevel == 0) {
			fprintf(logFile, "File %s with Damage Level 0 Suffix %c%d doesn't exist, trying fallback solution (strip suffix)\n", baseName, dmgChar, dmgLevel);
			sprintf(cfgFileName, "%s.cfg", baseName);
			skipDamageSuffix = TRUE;
		}
	}
	else
	{
		// part with damage levels (e.g. caps)
		sprintf( cfgFileName, "%s.cfg", baseName );
	}

	// Open configuration file
	cfgFile = fopen( cfgFileName, "r" );
	if( cfgFile == NULL )
	{
			fprintf( logFile, "****** ERROR: error opening cfg file %s\n", cfgFileName );
			fflush( logFile );
			numErrors++;
			return(0);
	}

	char outFileName[100];

	// ******* output file *******
	if( dmgLevel != -1 )
	{
		// part with damage levels
		lpBuf = UnicodeToAnsi(name);
		if (skipDamageSuffix)
			sprintf(outFileName, "%s.msh", lpBuf);
		else
			sprintf( outFileName, "%s_%c%d.msh", lpBuf, dmgChar, dmgLevel );
		delete(lpBuf);
	}
	else
	{
		// part with damage levels (e.g. caps)
		lpBuf = UnicodeToAnsi(name);
		sprintf( outFileName, "%s.msh", lpBuf ); 
		delete(lpBuf);
	}

	// Open the stream
	szBuf = AnsiToUnicode(outFileName);
	pStream = _tfopen(szBuf,_T("wt"));
	delete(szBuf);
	if (!pStream) {
		numErrors++;
		return 1;
	}

	// ******* mesh base name file *******
	if( dmgLevel != -1 )
	{
		// part with damage levels
		lpBuf = UnicodeToAnsi(name);
		if (skipDamageSuffix)
			sprintf(baseName, "%s", lpBuf);
		else
			sprintf( baseName, "%s_%c%d",lpBuf, dmgChar, dmgLevel );
		delete(lpBuf);
	}
	else
	{
		// part with damage levels (e.g. caps)
		lpBuf = UnicodeToAnsi(name);
		sprintf( baseName, "%s", lpBuf ); 
		delete(lpBuf);
	}

	fprintf(pStream, "// IL-2 export plugin \n");
	fprintf(pStream, "// Based on \"Maraz Exporter\" by 6S.Maraz - http://www.diavolirossi.net \n");
	fprintf(pStream, "// Further Development by SAS - https://www.sas1946.com \n");
	fprintf(pStream, "// Full Exporter Version ");
	lpBuf = UnicodeToAnsi(GetString(IDS_VERSIONSTRING));
	fprintf(pStream, lpBuf);
	delete(lpBuf);
	fprintf(pStream, " for 3DS Max ");
	lpBuf = UnicodeToAnsi(GetString(IDS_VALIDVERSIONS));
	fprintf(pStream, lpBuf);
	delete(lpBuf);
	fprintf(pStream, " \n");
	fprintf( pStream, "// Filename = %s\n\n", outFileName );

    fflush( pStream );
    fflush( logFile ); 

	// SCALE FACT FROM FILE NO LONGER USED
	// read left for compatibility only
	// read scale factor from cfg file
	// TODO: Changed by SAS~Storebror - Read Scale Factor in case of Single Mesh export
	// and in case it was not set (correctly) in project file
	if (scaleFact == 0.0F)
		fscanf( cfgFile, "%s %f", dummystr, &scaleFact);
	else
		fscanf(cfgFile, "%s %f", dummystr, &dummyFloat);
#ifdef DEBUG_LOD  
         fprintf( logFile, "Scale Factor: %f \n", scaleFact ); 
		 fflush( logFile ) ;
#endif

	// read number of LODs
	fscanf( cfgFile, "%s %d", dummystr, &nLODs ); 

#ifdef DEBUG_LOD 
    fprintf( logFile, "Number of LODs: %d \n", nLODs ); 
	fflush( logFile ) ;
#endif
	
	// read LOD distances
	fscanf( cfgFile, "%s", dummystr );
	for( k=0; k < nLODs; k++ )
	{
		fscanf( cfgFile, "%d", &(LODdist[k]) ); 
#ifdef DEBUG_LOD 
		fprintf( logFile, "LOD Dist: %d \n", LODdist[k]  ); 
		fflush( logFile ) ;
#endif
	}

	// read LOD suffix/prefix
	fscanf( cfgFile, "%s %c", dummystr, &LODStrType ); 
#ifdef DEBUG_LOD 
	fprintf( logFile, "LOD name type: |%c| \n", LODStrType  ); 
	fflush( logFile ) ;
#endif

	// read LOD suffix/prefix
	fscanf( cfgFile, "%s", dummystr );
	for( k = 0; k < nLODs; k++ )
	{
		fscanf( cfgFile, "%s", LODstr[k] ); 
#ifdef DEBUG_LOD 
		fprintf( logFile, "LOD str: %s \n", LODstr[k]  ); 
		fflush( logFile ) ;
#endif
	}
	
	// read number of collision objects
	fscanf( cfgFile, "%s %d", dummystr, &nCollObjects ); 

#ifdef DEBUG_LOD 
    fprintf( logFile, "Number of collision boxes: %d \n", nCollObjects ); 
	fflush( logFile ) ;
#endif

	// read collision boxes
	for( k = 0; k < nCollObjects; k++ )
	{
		fscanf( cfgFile, "%s", collObject[k] ); 
#ifdef DEBUG_LOD 
		fprintf( logFile, "LOD Dist: %s \n", collObject[k] ); 
		fflush( logFile ) ;
#endif
	}

	// read number of hooks
	fscanf( cfgFile, "%s %d", dummystr, &nHooks );
#ifdef DEBUG_LOD 
	fprintf( logFile, "number of hooks: %d \n", nHooks  ); 
	fflush( logFile ) ;
#endif

	// read name of hooks
	for( k = 0; k < nHooks; k++ )
	{
		fscanf( cfgFile, "%s", hooks[k] ); 
#ifdef DEBUG_LOD 
		fprintf( logFile, "Hook: %s \n", hooks[k] ); 
		fflush( logFile ) ;
#endif
	}

	// read character for shadow
	fscanf( cfgFile, "%s %c", dummystr, &shadowChar ); 
#ifdef DEBUG_LOD 
	fprintf( logFile, "character for shadow: |%c| \n", shadowChar  ); 
	fflush( logFile ) ;
#endif

	if( shadowChar == '7') // support for SM79
	{
		fscanf( cfgFile, "%s %s", dummystr, shadowBaseName ); 
#ifdef DEBUG_LOD 
		fprintf( logFile, "base name for shadow: |%s| \n", shadowBaseName  ); 
		fflush( logFile ) ;
#endif
	}
	// Startup the progress bar.
	ip->ProgressStart(GetString(IDS_PROGRESS_MSG), TRUE, fn, NULL);

	// Get a total node count by traversing the scene
	// We don't really need to do this, but it doesn't take long, and
	// it is nice to have an accurate progress bar.
	nTotalNodeCount = 0;
	nCurNode = 0;
	PreProcess(ip->GetRootNode(), nTotalNodeCount);

#ifdef DEBUG 
	fprintf( pStream, "//Node count: %d\n", nTotalNodeCount );
    fflush( pStream );
#endif

	// First we write out a file header with global information. 
	ExportGlobalInfo();

	// Export LOD section
    ExportLOD();

	currNode = ip->GetRootNode();
	int numChildren = ip->GetRootNode()->NumberOfChildren();

#ifdef DEBUG
	fprintf_UtoA( logFile, "Root Node = %s\n", currNode->GetName() );
	fprintf( logFile, "Num Children = %d\n", numChildren );
	fprintf( logFile, "Children:\n" );
	fflush( logFile );
#endif

	// Call our node enumerator.
	// The nodeEnum function will recurse into itself and 
	// export each object found in the scene.

#ifdef DEBUG_LOD
	fprintf( logFile, "Base name = %s\n", baseName );
	fflush( logFile );
#endif    

	/***********************************************/
    /********* BUILD NAME OF MAIN NODE *************/
    /***********************************************/
	if( nLODs > 0 )
		if( LODStrType == 's' || LODStrType == 'S')		// suffix
			sprintf( nodeName, "%s%s", baseName, LODstr[0] );
		else
			sprintf( nodeName, "%s%s", LODstr[0], baseName ); //prefix
	else
		strcpy( nodeName, baseName );	// no LOD

	if( dmgLevel == 0 ) //HOOKS only for D0
	{
		/***********************************************/
		/**************** EXPORT HOOKS *****************/
		/***********************************************/

		/****** print hook names *******/
		if( nHooks > 0 )
			fprintf( pStream, "\n[Hooks]\n" );

		for( k = 0; k < nHooks; k++ )
		{
			// name of hook  
			fprintf( pStream, "%s <BASE>\n", hooks[k] );
		}

		if( nHooks > 0 )
			fprintf( pStream, "\n[HookLoc]\n" );

		// get parent node of the hook
		LPCWSTR szBuf = AnsiToUnicode(nodeName);
		parentNode = ip->GetINodeByName( szBuf );
		delete(szBuf);
		if( parentNode == NULL )
		{
			fprintf( logFile, "****** ERROR: node %s not found\n", nodeName );
			numErrors++;
		}
		
		// export hooks coordinates
		for( k = 0; k < nHooks; k++ )
		{
			// name of hook to export 
			strcpy( nodeName, hooks[k] ); 
#ifdef DEBUG_LOD
			fprintf( logFile, "Hook Object to export = %s\n", nodeName );
#endif  

			szBuf = AnsiToUnicode(nodeName);
			newNode = ip->GetINodeByName( szBuf );
			delete(szBuf);
			if( newNode == NULL )
			{
				fprintf( logFile, "****** ERROR: node %s not found\n", nodeName );
				numErrors++;
			}
			else
			{
				//ExportHook( newNode, parentNode );  
				ExportHook( newNode, parentNode, nodeName );  
#ifdef DEBUG_LOD
				fprintf( logFile, "Done.\n" );
#endif  
			}
		}
	}
    /***********************************************/
    /************** EXPORT MAIN LOD ****************/
    /***********************************************/
	if( nLODs > 0 )
		if( LODStrType == 's' || LODStrType == 'S')		// suffix
			sprintf( nodeName, "%s%s", baseName, LODstr[0] );
		else
			sprintf( nodeName, "%s%s", LODstr[0], baseName ); //prefix
	else
		strcpy( nodeName, baseName );	// no LOD

#ifdef DEBUG_LOD
	fprintf( logFile, "Object to export = %s\n", nodeName );
	fflush( logFile );
#endif        

	szBuf = AnsiToUnicode(nodeName);
	newNode = ip->GetINodeByName( szBuf );
	delete(szBuf);
	if( newNode == NULL )
	{
		fprintf( logFile, "****** ERROR: node %s not found\n", nodeName );
		numErrors++;
	}
	else
	{
		// Store baseName as User Property for later Parent reference!
		newNode->SetUserPropString(_M("baseName"), CStr(baseName).ToMSTR());

/************************ PRINT HIER.HIM INFORMATION *********************/
		char undamagedName[100];

		fprintf( hierFile, "\n[%s]\n", baseName );
		fprintf( hierFile, "Mesh %s \n", baseName );
		
		if( dmgLevel <= 0 ) //only for D0 and caps
		{
			INode *parentNode = newNode->GetParentNode();
			if( parentNode == NULL || _tcsstr( parentNode->GetName(), _T("Root") ) != NULL )
				fprintf( hierFile, "Parent _ROOT_\n" );
			else {
				// try to get Parent's baseName
				TCHAR* szParentNodeName = (TCHAR*)parentNode->GetName();

				MSTR parentBaseName;
				if (parentNode->GetUserPropString(_M("baseName"), parentBaseName)) {
					szParentNodeName = parentBaseName.ToBSTR();
				} else {
					if( nLODs > 0 ) {
						if( LODStrType == 's' || LODStrType == 'S') {
							parentBaseName = MSTR(parentNode->GetName());
							MSTR lodName = CStr(LODstr[0]).ToBSTR();
							if (parentBaseName.EndsWith(lodName)) {
								parentBaseName.remove(parentBaseName.length() - lodName.length());
								szParentNodeName = parentBaseName.ToBSTR();
							}
						} else {
							parentBaseName = MSTR(parentNode->GetName());
							MSTR lodName = CStr(LODstr[0]).ToBSTR();
							if (parentBaseName.StartsWith(lodName)) {
								parentBaseName.remove(0, lodName.length());
								szParentNodeName = parentBaseName.ToBSTR();
							}
						}
					}
				}

				fprintf_UtoA( hierFile, "Parent %s\n", szParentNodeName);
				//fprintf_UtoA( hierFile, "Parent %s\n", parentNode->GetName());
			}
		}
		else
		{
			// damage levels are always children of undamaged
			strcpy ( undamagedName, baseName );

			char *ptr = strstr( undamagedName, "_D" );
			if (ptr == NULL )
				ptr = strstr( undamagedName, "_d" );

			sprintf( ptr, "_%c0\0", dmgChar );

			fprintf( hierFile, "Parent %s\n", undamagedName );
		}

/************************ PRINT HIER.HIM INFORMATION *********************/
	    
		/******* CALL THE NODE EXPORTER *******/
		ExportNode( newNode, "" );    

#ifdef DEBUG_LOD
		fprintf( logFile, "Done.\n" ); 
		fflush( logFile );
#endif  	      
	}
    /*****************************************************/ 
    /**************** EXPORT main SHADOW *****************/
    /*****************************************************/
	if( shadowChar == '7' )
		sprintf( shadowNodeName, "%s%s", shadowBaseName, LODstr[0] );  
	else
		sprintf( shadowNodeName, "%c%s", shadowChar, nodeName );

#ifdef DEBUG_LOD
	fprintf( logFile, "Shadow Object to export = %s\n", shadowNodeName );
	fflush( logFile );
#endif    
	
	szBuf = AnsiToUnicode(shadowNodeName);
	newNode = ip->GetINodeByName( szBuf );
	delete(szBuf);
	if( newNode == NULL )
	{
		fprintf( logFile, "****** WARNING: shadow node %s not found\n", shadowNodeName );
		numWarnings++;
	}
	else
	{
		/********* CALL THE SHADOW EXPORTER ********/ 
		ExportShadowNode( newNode, "" );    
#ifdef DEBUG_LOD
		fprintf( logFile, "Done.\n" );
		fflush( logFile );
#endif  
	}

    /***********************************************/
    /**************** EXPORT LOD n *****************/
    /***********************************************/
	for( k = 1; k < nLODs; k++ )
	{
		if( LODStrType == 's' || LODStrType == 'S')		// suffix
			sprintf( nodeName, "%s%s", baseName, LODstr[k] );
		else
			sprintf( nodeName, "%s%s", LODstr[k], baseName );		

#ifdef DEBUG_LOD
		fprintf( logFile, "Object to export = %s\n", nodeName );
#endif  

		szBuf = AnsiToUnicode(nodeName);
		newNode = ip->GetINodeByName( szBuf );
		delete(szBuf);
		if( newNode == NULL )
		{
			fprintf( logFile, "****** ERROR: node %s not found\n", nodeName );
			numErrors++;
		}
		else
		{
			sprintf( LODstring, "LOD%d_", k );
			
			/******** CALL THE LOD NODE EXPORTER *******/
			ExportNode( newNode, LODstring );  

#ifdef DEBUG_LOD
			fprintf( logFile, "Done.\n" );
#endif  
		}
		/*****************************************************/
		/**************** EXPORT LOD N SHADOW ****************/
		/*****************************************************/
		if( shadowChar == '7' )
			sprintf( shadowNodeName, "%s%s", shadowBaseName, LODstr[k] );  
		else
			sprintf( shadowNodeName, "%c%s", shadowChar, nodeName ); 

#ifdef DEBUG_LOD
		fprintf( logFile, "Shadow Object to export = %s\n", shadowNodeName );
		fflush( logFile );
#endif        

		szBuf = AnsiToUnicode(shadowNodeName);
		newNode = ip->GetINodeByName( szBuf );
		delete(szBuf);
		if( newNode == NULL )
		{
			fprintf( logFile, "****** WARNING: shadow node %s not found\n", shadowNodeName );
			numWarnings++;
		}
		else
		{
			sprintf( LODstring, "LOD%d_", k );

			/******* CALL THE SHADOW NODE EXPORTER *******/
			ExportShadowNode( newNode, LODstring );    

#ifdef DEBUG_LOD
			fprintf( logFile, "Done.\n" );
			fflush( logFile );
#endif
		}
	}	
	
	/***********************************************/
	/************ EXPORT COLLISION BOXES ***********/
	/***********************************************/

	if( nCollObjects > 0 )
	{	
   		// print heading information
   		fprintf( pStream, "\n[CoCommon]\n" );
   		fprintf( pStream, "NBlocks 1\n\n" );
   	
   		fprintf( pStream, "[CoCommon_b0]\n" );
   		fprintf( pStream, "NParts %d\n", nCollObjects );		
	}
	

	for( k = 0; k < nCollObjects ; k++)
	{
#ifdef DEBUG_LOD
		fprintf( logFile, "Collision Object to export = %s\n", collObject[k] );
#endif 		

		szBuf = AnsiToUnicode(collObject[k]);
		newNode = ip->GetINodeByName( szBuf );
		delete(szBuf);
		if( newNode == NULL )
		{
			fprintf( logFile, "****** ERROR: collision node %s not found\n", collObject[k] );
			numErrors++;
		}
		else
		{
			/****** CALL THE COLLISION NODE EXPORTER *******/
			ExportCollNode( newNode, k );

/************************ PRINT HIER.HIM INFORMATION *********************/
			if( strstr( collObject[k], "xx") != NULL )
					fprintf( hierFile, "CollisionObject .%s\n", collObject[k] );
			else		
					fprintf( hierFile, "CollisionObject %s\n", collObject[k] );
/************************ PRINT HIER.HIM INFORMATION *********************/
					  
#ifdef DEBUG_LOD
			fprintf( logFile, "Done.\n" );
#endif  
		}
	}

	// We're done. Finish the progress bar.
	ip->ProgressEnd();

	// Close the stream
	fprintf(pStream, "\n\n; eof" );
	fclose(pStream);
	fclose (cfgFile); 
	fclose (hierFile); 
	//fclose (logFile); 

	return 1;
}

BOOL AsciiExp::SupportsOptions(int ext, DWORD options) {
	assert(ext == 0);	// We only support one extension
	return(options == SCENE_EXPORT_SELECTED) ? TRUE : FALSE;
	}

// This method is the main object exporter.
// It is called once of every node in the scene. The objects are
// exported as they are encoutered.

// Before recursing into the children of a node, we will export it.
// The benefit of this is that a nodes parent is always before the
// children in the resulting file. This is desired since a child's
// transformation matrix is optionally relative to the parent.

BOOL AsciiExp::nodeEnum(INode* node, int indentLevel) 
{
	if(exportSelected && node->Selected() == FALSE)
		return TREE_CONTINUE;

	nCurNode++;
	ip->ProgressUpdate((int)((float)nCurNode/nTotalNodeCount*100.0f)); 

	// Stop recursing if the user pressed Cancel 
	if (ip->GetCancel())
		return FALSE;
	
	fprintf_UtoA(pStream,"%s\n",  FixupName((TCHAR*)node->GetName())); 
	
/*******************	
	// Only export if exporting everything or it's selected
	if(!exportSelected || node->Selected()) {

		// The ObjectState is a 'thing' that flows down the pipeline containing
		// all information about the object. By calling EvalWorldState() we tell
		// max to eveluate the object at end of the pipeline.
		ObjectState os = node->EvalWorldState(0); 

		// The obj member of ObjectState is the actual object we will export.
		if (os.obj) {

			// We look at the super class ID to determine the type of the object.
			switch(os.obj->SuperClassID()) {
			case GEOMOBJECT_CLASS_ID: 
				if (GetIncludeObjGeom()) ExportGeomObject(node, indentLevel); 
				break;
			case CAMERA_CLASS_ID:
				if (GetIncludeObjCamera()) ExportCameraObject(node, indentLevel); 
				break;
			case LIGHT_CLASS_ID:
				if (GetIncludeObjLight()) ExportLightObject(node, indentLevel); 
				break;
			case SHAPE_CLASS_ID:
				if (GetIncludeObjShape()) ExportShapeObject(node, indentLevel); 
				break;
			case HELPER_CLASS_ID:
				if (GetIncludeObjHelper()) ExportHelperObject(node, indentLevel); 
				break;
			}
		}
	}	
******************************/	

	// For each child of this node, we recurse into ourselves 
	// until no more children are found.
	for (int c = 0; c < node->NumberOfChildren(); c++) {
		if (!nodeEnum(node->GetChildNode(c), indentLevel))
			return FALSE;
	}
	
	// If thie is true here, it is the end of the group we started above.
	if (node->IsGroupHead()) {
		fprintf(pStream,"\n" );
	}

	return TRUE;
}


void AsciiExp::PreProcess(INode* node, int& nodeCount)
{
	nodeCount++;
	
	// Add the nodes material to out material list
	// Null entries are ignored when added...
	mtlList.AddMtl(node->GetMtl());

	// For each child of this node, we recurse into ourselves 
	// and increment the counter until no more children are found.
	for (int c = 0; c < node->NumberOfChildren(); c++) {
		PreProcess(node->GetChildNode(c), nodeCount);
	}
}

/****************************************************************************

 Configuration.
 To make all options "sticky" across sessions, the options are read and
 written to a configuration file every time the exporter is executed.

 ****************************************************************************/

TSTR AsciiExp::GetCfgFilename()
{
	TSTR filename;
	
	filename += ip->GetDir(APP_PLUGCFG_DIR);
	filename += _T("\\");
	filename += CFGFILENAME;

	return filename;
}

// NOTE: Update anytime the CFG file changes
#define CFG_VERSION 0x03

BOOL AsciiExp::ReadConfig()
{
	TSTR filename = GetCfgFilename();
	FILE* cfgStream;

	cfgStream = fopen(filename.ToCStr().data(), "rb");
	if (!cfgStream)
		return FALSE;

	// First item is a file version
	int fileVersion = _getw(cfgStream);

	if (fileVersion > CFG_VERSION) {
		// Unknown version
		fclose(cfgStream);
		return FALSE;
	}

	SetIncludeMesh(fgetc(cfgStream));
	SetIncludeAnim(fgetc(cfgStream));
	SetIncludeMtl(fgetc(cfgStream));
	SetIncludeMeshAnim(fgetc(cfgStream));
	SetIncludeCamLightAnim(fgetc(cfgStream));
	SetIncludeIKJoints(fgetc(cfgStream));
	SetIncludeNormals(fgetc(cfgStream));
	SetIncludeTextureCoords(fgetc(cfgStream));
	SetIncludeObjGeom(fgetc(cfgStream));
	SetIncludeObjShape(fgetc(cfgStream));
	SetIncludeObjCamera(fgetc(cfgStream));
	SetIncludeObjLight(fgetc(cfgStream));
	SetIncludeObjHelper(fgetc(cfgStream));
	SetAlwaysSample(fgetc(cfgStream));
	SetMeshFrameStep(_getw(cfgStream));
	SetKeyFrameStep(_getw(cfgStream));

	// Added for version 0x02
	if (fileVersion > 0x01) {
		SetIncludeVertexColors(fgetc(cfgStream));
	}

	// Added for version 0x03
	if (fileVersion > 0x02) {
		SetPrecision(_getw(cfgStream));
	}

	fclose(cfgStream);

	return TRUE;
}

void AsciiExp::WriteConfig()
{
	TSTR filename = GetCfgFilename();
	FILE* cfgStream;

	cfgStream = fopen(filename.ToCStr().data(), "wb");
	if (!cfgStream)
		return;

	// Write CFG version
	_putw(CFG_VERSION,				cfgStream);

	fputc(GetIncludeMesh(),			cfgStream);
	fputc(GetIncludeAnim(),			cfgStream);
	fputc(GetIncludeMtl(),			cfgStream);
	fputc(GetIncludeMeshAnim(),		cfgStream);
	fputc(GetIncludeCamLightAnim(),	cfgStream);
	fputc(GetIncludeIKJoints(),		cfgStream);
	fputc(GetIncludeNormals(),		cfgStream);
	fputc(GetIncludeTextureCoords(),	cfgStream);
	fputc(GetIncludeObjGeom(),		cfgStream);
	fputc(GetIncludeObjShape(),		cfgStream);
	fputc(GetIncludeObjCamera(),	cfgStream);
	fputc(GetIncludeObjLight(),		cfgStream);
	fputc(GetIncludeObjHelper(),	cfgStream);
	fputc(GetAlwaysSample(),		cfgStream);
	_putw(GetMeshFrameStep(),		cfgStream);
	_putw(GetKeyFrameStep(),		cfgStream);
	fputc(GetIncludeVertexColors(),	cfgStream);
	_putw(GetPrecision(),			cfgStream);

	fclose(cfgStream);
}

BOOL AsciiExp::EndsWith(const char* haystack, const char* needle)
{
	size_t haystacklen = strlen(haystack);
	size_t needlelen = strlen(needle);
	if (needlelen > haystacklen) return false;
	return (strcmp(haystack + haystacklen - needlelen, needle) == 0) ? TRUE : FALSE;
}

BOOL AsciiExp::StartsWith(const char* haystack, const char* needle)
{
	size_t haystacklen = strlen(haystack);
	size_t needlelen = strlen(needle);
	if (needlelen > haystacklen) return false;
	return (strncmp(haystack, needle, needlelen) == 0) ? TRUE : FALSE;
}

BOOL AsciiExp::FileExists(const char* fileName)
{
	return((GetFileAttributesA(fileName) != INVALID_FILE_ATTRIBUTES) && (GetFileAttributesA(fileName) != FILE_ATTRIBUTE_DIRECTORY));
}

BOOL MtlKeeper::AddMtl(Mtl* mtl)
{
	if (!mtl) {
		return FALSE;
	}

	int numMtls = mtlTab.Count();
	for (int i=0; i<numMtls; i++) {
		if (mtlTab[i] == mtl) {
			return FALSE;
		}
	}
	mtlTab.Append(1, &mtl, 25);

	return TRUE;
}

int MtlKeeper::GetMtlID(Mtl* mtl)
{
	int numMtls = mtlTab.Count();
	for (int i=0; i<numMtls; i++) {
		if (mtlTab[i] == mtl) {
			return i;
		}
	}
	return -1;
}

int MtlKeeper::Count()
{
	return mtlTab.Count();
}

Mtl* MtlKeeper::GetMtl(int id)
{
	return mtlTab[id];
}
