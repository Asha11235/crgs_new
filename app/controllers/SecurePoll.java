package controllers;

import com.google.gson.Gson;
import models.PollDefination;
import models.PollVoteReply;
import play.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by asha on 4/16/17.
 */
public class SecurePoll {

    public static String loadPoll() throws SQLException {

        String mp = "";

        mp = PollDefination.getPollReport();

        Gson gson = new Gson();

        return gson.toJson(mp);
    }



    public static String loadPollGraph() throws SQLException {


        // Logger.info("gender: " + gender + " age: " + age);


        String mp = "";

        mp = PollDefination.getPollGraphData();

        Gson gson = new Gson();

        return gson.toJson(mp);

    }

    public static String loadPollGraphResult(String pollId,String gender,String age)throws SQLException {

        String mp = "";

        mp = PollDefination.getPollGraphResultData(pollId,gender,age);

        Gson gson = new Gson();

        return gson.toJson(mp);
    }
    public static void voteReply(String gender, String option, String age,String pollId){


        pollId=pollId.trim();
        Logger.info("PollId: " + pollId);
        Long pollid= Long.parseLong(pollId);
        PollDefination polldef = PollDefination.findById(pollid);

        PollVoteReply pollvotereply = new PollVoteReply();

        pollvotereply.gender=gender;
        pollvotereply.age=age;
        pollvotereply.poll=polldef;
        pollvotereply.answer=option;

        pollvotereply.save();





        //Forms.landingPage();
    }

}
