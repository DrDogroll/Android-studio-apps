package com.dogroll.admin.vetlauncher;

import android.content.ContentValues;
import android.content.Context;

import java.util.Calendar;

public class cpdProcessor {
    
    ContentValues processCDP (Context context) {
        
        double
                cpdthisCLAP2P, cpdthisCLAMentor, cpdthisCLAActivity, cpdthisCLAMeeting, cpdthisCLAComponents, cpdthisCLAManagement, cpdthisCLAOther, cpdthisCVECourse, cpdthisCVETraining, cpdthisCVEConference, cpdthisCVEPresentation, cpdthisCVEPublications, cpdthisCVERefereeing, cpdthisCVELearning, cpdthisCVEOther, cpdthisSDLUpdate, cpdthisSDLLearning, cpdthisSDLResearch, cpdthisSDLPublications, cpdthisSDLPlan, cpdthisSDLRecording, cpdthisSDLOther,
                cpdlastCLAP2P, cpdlastCLAMentor, cpdlastCLAActivity, cpdlastCLAMeeting, cpdlastCLAComponents, cpdlastCLAManagement, cpdlastCLAOther, cpdlastCVECourse, cpdlastCVETraining, cpdlastCVEConference, cpdlastCVEPresentation, cpdlastCVEPublications, cpdlastCVERefereeing, cpdlastCVELearning, cpdlastCVEOther, cpdlastSDLUpdate, cpdlastSDLLearning, cpdlastSDLResearch, cpdlastSDLPublications, cpdlastSDLPlan, cpdlastSDLRecording, cpdlastSDLOther,
                cpdprevlastCLAP2P, cpdprevlastCLAMentor, cpdprevlastCLAActivity, cpdprevlastCLAMeeting, cpdprevlastCLAComponents, cpdprevlastCLAManagement, cpdprevlastCLAOther, cpdprevlastCVECourse, cpdprevlastCVETraining, cpdprevlastCVEConference, cpdprevlastCVEPresentation, cpdprevlastCVEPublications, cpdprevlastCVERefereeing, cpdprevlastCVELearning, cpdprevlastCVEOther, cpdprevlastSDLUpdate, cpdprevlastSDLLearning, cpdprevlastSDLResearch, cpdprevlastSDLPublications, cpdprevlastSDLPlan, cpdprevlastSDLRecording, cpdprevlastSDLOther,
                cpdprevprevlastCLAP2P, cpdprevprevlastCLAMentor, cpdprevprevlastCLAActivity, cpdprevprevlastCLAMeeting, cpdprevprevlastCLAComponents, cpdprevprevlastCLAManagement, cpdprevprevlastCLAOther, cpdprevprevlastCVECourse, cpdprevprevlastCVETraining, cpdprevprevlastCVEConference, cpdprevprevlastCVEPresentation, cpdprevprevlastCVEPublications, cpdprevprevlastCVERefereeing, cpdprevprevlastCVELearning, cpdprevprevlastCVEOther, cpdprevprevlastSDLUpdate, cpdprevprevlastSDLLearning, cpdprevprevlastSDLResearch, cpdprevprevlastSDLPublications, cpdprevprevlastSDLPlan, cpdprevprevlastSDLRecording, cpdprevprevlastSDLOther;


        Calendar calendar = Calendar.getInstance();
        int yearInt = calendar.get(Calendar.YEAR);
        String year = Integer.toString(yearInt);
        String yearlast = Integer.toString(yearInt - 1);
        String yearprevlast = Integer.toString(yearInt - 2);
        String yearprevprevlast = Integer.toString(yearInt - 3);

        DBHandler MasterDB = new DBHandler(context);
        
        //values with limitations
        cpdlastCLAMentor = MasterDB.getcpdValues(yearlast,"CLAMentor");
        cpdlastCLAMeeting = MasterDB.getcpdValues(yearlast,"CLAMeeting");
        cpdlastCVEPublications = MasterDB.getcpdValues(yearlast, "CVEPublications");
        cpdlastCVERefereeing = MasterDB.getcpdValues(yearlast, "CVERefereeing");
        cpdlastSDLPublications = MasterDB.getcpdValues(yearlast,"SDLPublications");
        cpdprevlastCLAMentor = MasterDB.getcpdValues(yearprevlast,"CLAMentor");
        cpdprevlastCLAMeeting = MasterDB.getcpdValues(yearprevlast,"CLAMeeting");
        cpdprevlastCVEPublications = MasterDB.getcpdValues(yearprevlast, "CVEPublications");
        cpdprevlastCVERefereeing = MasterDB.getcpdValues(yearprevlast, "CVERefereeing");
        cpdprevlastSDLPublications = MasterDB.getcpdValues(yearprevlast,"SDLPublications");
        cpdprevprevlastCLAMentor = MasterDB.getcpdValues(yearprevprevlast,"CLAMentor");
        cpdprevprevlastCLAMeeting = MasterDB.getcpdValues(yearprevprevlast,"CLAMeeting");
        cpdprevprevlastCVEPublications = MasterDB.getcpdValues(yearprevprevlast, "CVEPublications");
        cpdprevprevlastCVERefereeing = MasterDB.getcpdValues(yearprevprevlast, "CVERefereeing");
        cpdprevprevlastSDLPublications = MasterDB.getcpdValues(yearprevprevlast,"SDLPublications");
        
        //trimming limitations
        if (cpdprevprevlastCLAMentor > 9)
            cpdprevprevlastCLAMentor = 9;
        if (cpdprevprevlastCLAMentor + cpdprevlastCLAMentor > 9)
            cpdprevlastCLAMentor = 9 - cpdprevprevlastCLAMentor;
        if (cpdprevprevlastCLAMentor + cpdprevlastCLAMentor + cpdlastCLAMentor > 9)
           cpdlastCLAMentor = 9 - (cpdprevprevlastCLAMentor + cpdprevlastCLAMentor);

        if (cpdprevprevlastCLAMeeting > 9)
            cpdprevprevlastCLAMeeting = 9;
        if (cpdprevprevlastCLAMeeting + cpdprevlastCLAMeeting > 9)
            cpdprevlastCLAMeeting = 9 - cpdprevprevlastCLAMeeting;
        if (cpdprevprevlastCLAMeeting + cpdprevlastCLAMeeting + cpdlastCLAMeeting > 9)
            cpdlastCLAMeeting = 9 - (cpdprevprevlastCLAMeeting + cpdprevlastCLAMeeting);

        if (cpdprevprevlastCVEPublications > 20)
            cpdprevprevlastCVEPublications = 20;
        if (cpdprevprevlastCVEPublications + cpdprevlastCVEPublications > 20)
            cpdprevlastCVEPublications = 20 - cpdprevprevlastCVEPublications;
        if (cpdprevprevlastCVEPublications + cpdprevlastCVEPublications + cpdlastCVEPublications > 20)
            cpdlastCVEPublications = 20 - (cpdprevprevlastCVEPublications + cpdprevlastCVEPublications);

        if (cpdprevprevlastCVERefereeing > 4)
            cpdprevprevlastCVERefereeing = 4;
        if (cpdprevprevlastCVERefereeing + cpdprevlastCVERefereeing > 4)
            cpdprevlastCVERefereeing = 4 - cpdprevprevlastCVERefereeing;
        if (cpdprevprevlastCVERefereeing + cpdprevlastCVERefereeing + cpdlastCVERefereeing > 4)
            cpdlastCVERefereeing = 4 - (cpdprevprevlastCVERefereeing = cpdprevlastCVERefereeing);

        if (cpdprevprevlastSDLPublications > 20)
            cpdprevprevlastSDLPublications = 20;
        if (cpdprevprevlastSDLPublications + cpdprevlastSDLPublications > 20)
            cpdprevlastSDLPublications = 20 - cpdprevprevlastSDLPublications;
        if (cpdprevprevlastSDLPublications + cpdprevlastSDLPublications + cpdlastSDLPublications > 20)
            cpdlastSDLPublications = 20 - (cpdprevprevlastSDLPublications + cpdprevlastSDLPublications);
        
        //this years CLA
        cpdthisCLAP2P = MasterDB.getcpdValues(year,"CLAP2P");
        cpdthisCLAMentor = MasterDB.getcpdValues(year,"CLAMentor");
        cpdthisCLAActivity = MasterDB.getcpdValues(year,"CLAActivity");
        cpdthisCLAMeeting = MasterDB.getcpdValues(year,"CLAMeeting");
        cpdthisCLAComponents = MasterDB.getcpdValues(year, "CLAComponents");
        cpdthisCLAManagement = MasterDB.getcpdValues(year,"CLAManagement");
        cpdthisCLAOther = MasterDB.getcpdValues(year,"CLAOther");
        
        //this years CVE
        cpdthisCVECourse = MasterDB.getcpdValues(year,"CVECourse");
        cpdthisCVETraining = MasterDB.getcpdValues(year, "CVETraining");
        cpdthisCVEConference = MasterDB.getcpdValues(year,"CVEConference");
        cpdthisCVEPresentation = MasterDB.getcpdValues(year,"CVEPresentation");
        cpdthisCVEPublications = MasterDB.getcpdValues(year, "CVEPublications");
        cpdthisCVERefereeing = MasterDB.getcpdValues(year, "CVERefereeing");
        cpdthisCVELearning = MasterDB.getcpdValues(year, "CVELearning");
        cpdthisCVEOther = MasterDB.getcpdValues(year, "CVEOther");
        
        //this years SDL
        cpdthisSDLUpdate = MasterDB.getcpdValues(year, "SDLUpdate");
        cpdthisSDLLearning = MasterDB.getcpdValues(year, "SDLLearning");
        cpdthisSDLResearch = MasterDB.getcpdValues(year, "SDLResearch");
        cpdthisSDLPublications = MasterDB.getcpdValues(year,"SDLPublications");
        cpdthisSDLPlan = MasterDB.getcpdValues(year, "SDLPlan");
        cpdthisSDLRecording = MasterDB.getcpdValues(year, "SDLRecording");
        cpdthisSDLOther = MasterDB.getcpdValues(year, "SDLOther");
        
        if(cpdthisSDLPlan > 2)
            cpdthisSDLPlan = 2;
            

        //last years CLA
        cpdlastCLAP2P = MasterDB.getcpdValues(yearlast,"CLAP2P");
        cpdlastCLAActivity = MasterDB.getcpdValues(yearlast,"CLAActivity");
        cpdlastCLAComponents = MasterDB.getcpdValues(yearlast, "CLAComponents");
        cpdlastCLAManagement = MasterDB.getcpdValues(yearlast,"CLAManagement");
        cpdlastCLAOther = MasterDB.getcpdValues(yearlast,"CLAOther");
        //last years CVE
        cpdlastCVECourse = MasterDB.getcpdValues(yearlast,"CVECourse");
        cpdlastCVETraining = MasterDB.getcpdValues(yearlast, "CVETraining");
        cpdlastCVEConference = MasterDB.getcpdValues(yearlast,"CVEConference");
        cpdlastCVEPresentation = MasterDB.getcpdValues(yearlast,"CVEPresentation");
        cpdlastCVELearning = MasterDB.getcpdValues(yearlast, "CVELearning");
        cpdlastCVEOther = MasterDB.getcpdValues(yearlast, "CVEOther");
        //last years SDL
        cpdlastSDLUpdate = MasterDB.getcpdValues(yearlast, "SDLUpdate");
        cpdlastSDLLearning = MasterDB.getcpdValues(yearlast, "SDLLearning");
        cpdlastSDLResearch = MasterDB.getcpdValues(yearlast, "SDLResearch");
        cpdlastSDLPlan = MasterDB.getcpdValues(yearlast, "SDLPlan");
        cpdlastSDLRecording = MasterDB.getcpdValues(yearlast, "SDLRecording");
        cpdlastSDLOther = MasterDB.getcpdValues(yearlast, "SDLOther");

        if(cpdlastSDLPlan > 2)
            cpdlastSDLPlan = 2;

        //previous to last years CLA
        cpdprevlastCLAP2P = MasterDB.getcpdValues(yearprevlast, "CLAP2P");
        cpdprevlastCLAActivity = MasterDB.getcpdValues(yearprevlast, "CLAActivity");
        cpdprevlastCLAComponents = MasterDB.getcpdValues(yearprevlast, "CLAComponents");
        cpdprevlastCLAManagement = MasterDB.getcpdValues(yearprevlast, "CLAManagement");
        cpdprevlastCLAOther = MasterDB.getcpdValues(yearprevlast, "CLAOther");
        //CVE
        cpdprevlastCVECourse = MasterDB.getcpdValues(yearprevlast, "CVECourse");
        cpdprevlastCVETraining = MasterDB.getcpdValues(yearprevlast, "CVETraining");
        cpdprevlastCVEConference = MasterDB.getcpdValues(yearprevlast, "CVEConference");
        cpdprevlastCVEPresentation = MasterDB.getcpdValues(yearprevlast, "CVEPresentation");
        cpdprevlastCVELearning = MasterDB.getcpdValues(yearprevlast, "CVELearning");
        cpdprevlastCVEOther = MasterDB.getcpdValues(yearprevlast, "CVEOther");
        //SDL
        cpdprevlastSDLUpdate = MasterDB.getcpdValues(yearprevlast, "SDLUpdate");
        cpdprevlastSDLLearning = MasterDB.getcpdValues(yearprevlast, "SDLLearning");
        cpdprevlastSDLResearch = MasterDB.getcpdValues(yearprevlast, "SDLResearch");
        cpdprevlastSDLPlan = MasterDB.getcpdValues(yearprevlast, "SDLPlan");
        cpdprevlastSDLRecording = MasterDB.getcpdValues(yearprevlast, "SDLRecording");
        cpdprevlastSDLOther = MasterDB.getcpdValues(yearprevlast, "SDLOther");
        if (cpdprevlastSDLPlan > 2)
            cpdprevlastSDLPlan = 2;

        //previous to previous last years CLA
        cpdprevprevlastCLAP2P = MasterDB.getcpdValues(yearprevprevlast, "CLAP2P");
        cpdprevprevlastCLAActivity = MasterDB.getcpdValues(yearprevprevlast, "CLAActivity");
        cpdprevprevlastCLAComponents = MasterDB.getcpdValues(yearprevprevlast, "CLAComponents");
        cpdprevprevlastCLAManagement = MasterDB.getcpdValues(yearprevprevlast, "CLAManagement");
        cpdprevprevlastCLAOther = MasterDB.getcpdValues(yearprevprevlast, "CLAOther");
        //CVE
        cpdprevprevlastCVECourse = MasterDB.getcpdValues(yearprevprevlast, "CVECourse");
        cpdprevprevlastCVETraining = MasterDB.getcpdValues(yearprevprevlast, "CVETraining");
        cpdprevprevlastCVEConference = MasterDB.getcpdValues(yearprevprevlast, "CVEConference");
        cpdprevprevlastCVEPresentation = MasterDB.getcpdValues(yearprevprevlast, "CVEPresentation");
        cpdprevprevlastCVELearning = MasterDB.getcpdValues(yearprevprevlast, "CVELearning");
        cpdprevprevlastCVEOther = MasterDB.getcpdValues(yearprevprevlast, "CVEOther");
        //SDL
        cpdprevprevlastSDLUpdate = MasterDB.getcpdValues(yearprevprevlast, "SDLUpdate");
        cpdprevprevlastSDLLearning = MasterDB.getcpdValues(yearprevprevlast, "SDLLearning");
        cpdprevprevlastSDLResearch = MasterDB.getcpdValues(yearprevprevlast, "SDLResearch");
        cpdprevprevlastSDLPlan = MasterDB.getcpdValues(yearprevprevlast, "SDLPlan");
        cpdprevprevlastSDLRecording = MasterDB.getcpdValues(yearprevprevlast, "SDLRecording");
        cpdprevprevlastSDLOther = MasterDB.getcpdValues(yearprevprevlast, "SDLOther");
        if (cpdprevprevlastSDLPlan > 2)
            cpdprevprevlastSDLPlan = 2;

        double cpdthisCLA, cpdthisCVE, cpdthisSDL, cpdlastCLA, cpdlastCVE, cpdlastSDL, cpdprevlastCLA, cpdprevlastCVE, cpdprevlastSDL, cpdprevprevlastCLA, cpdprevprevlastCVE, cpdprevprevlastSDL;

        cpdthisCLA = cpdthisCLAP2P + cpdthisCLAMentor + cpdthisCLAActivity + cpdthisCLAMeeting + cpdthisCLAComponents + cpdthisCLAManagement + cpdthisCLAOther;
        cpdthisCVE = cpdthisCVECourse + cpdthisCVETraining + cpdthisCVEConference + cpdthisCVEPresentation + cpdthisCVEPublications + cpdthisCVERefereeing + cpdthisCVELearning + cpdthisCVEOther;
        cpdthisSDL = cpdthisSDLUpdate + cpdthisSDLLearning + cpdthisSDLResearch + cpdthisSDLRecording + cpdthisSDLOther + cpdthisSDLPlan + cpdthisSDLPublications;

        cpdlastCLA = cpdlastCLAP2P + cpdlastCLAMentor + cpdlastCLAActivity + cpdlastCLAMeeting + cpdlastCLAComponents + cpdlastCLAManagement + cpdlastCLAOther;
        cpdlastCVE = cpdlastCVECourse + cpdlastCVETraining + cpdlastCVEConference + cpdlastCVEPresentation + cpdlastCVEPublications + cpdlastCVERefereeing + cpdlastCVELearning + cpdlastCVEOther;
        cpdlastSDL = cpdlastSDLUpdate + cpdlastSDLLearning + cpdlastSDLResearch + cpdlastSDLRecording + cpdlastSDLOther + cpdlastSDLPlan + cpdlastSDLPublications;

        cpdprevlastCLA = cpdprevlastCLAP2P + cpdprevlastCLAMentor + cpdprevlastCLAActivity + cpdprevlastCLAMeeting + cpdprevlastCLAComponents + cpdprevlastCLAManagement + cpdprevlastCLAOther;
        cpdprevlastCVE = cpdprevlastCVECourse + cpdprevlastCVETraining + cpdprevlastCVEConference + cpdprevlastCVEPresentation + cpdprevlastCVEPublications + cpdprevlastCVERefereeing + cpdprevlastCVELearning + cpdprevlastCVEOther;
        cpdprevlastSDL = cpdprevlastSDLUpdate + cpdprevlastSDLLearning + cpdprevlastSDLResearch + cpdprevlastSDLRecording + cpdprevlastSDLOther + cpdprevlastSDLPlan + cpdprevlastSDLPublications;

        cpdprevprevlastCLA = cpdprevprevlastCLAP2P + cpdprevprevlastCLAMentor + cpdprevprevlastCLAActivity + cpdprevprevlastCLAMeeting + cpdprevprevlastCLAComponents + cpdprevprevlastCLAManagement + cpdprevprevlastCLAOther;
        cpdprevprevlastCVE = cpdprevprevlastCVECourse + cpdprevprevlastCVETraining + cpdprevprevlastCVEConference + cpdprevprevlastCVEPresentation + cpdprevprevlastCVEPublications + cpdprevprevlastCVERefereeing + cpdprevprevlastCVELearning + cpdprevprevlastCVEOther;
        cpdprevprevlastSDL = cpdprevprevlastSDLUpdate + cpdprevprevlastSDLLearning + cpdprevprevlastSDLResearch + cpdprevprevlastSDLRecording + cpdprevprevlastSDLOther + cpdprevprevlastSDLPlan + cpdprevprevlastSDLPublications;

        ContentValues results = new ContentValues();
        results.put("thisCLA", cpdthisCLA);
        results.put("thisCVE", cpdthisCVE);
        results.put("thisSDL", cpdthisSDL);
        results.put("lastCLA", cpdlastCLA);
        results.put("lastCVE", cpdlastCVE);
        results.put("lastSDL", cpdlastSDL);
        results.put("prevlastCLA", cpdprevlastCLA);
        results.put("prevlastCVE", cpdprevlastCVE);
        results.put("prevlastSDL", cpdprevlastSDL);
        results.put("prevprevlastCLA", cpdprevprevlastCLA);
        results.put("prevprevlastCVE", cpdprevprevlastCVE);
        results.put("prevprevlastSDL", cpdprevprevlastSDL);

        return results;
    }
}
