package tech.codingclub.helix.controller;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.jooq.Condition;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import tech.codingclub.helix.database.GenericDB;
import tech.codingclub.helix.entity.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: rishabh
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {



    @RequestMapping(method = RequestMethod.POST, value = "/create-post")
    public
    @ResponseBody
    String createTweet(@RequestBody String data, HttpServletRequest request, HttpServletResponse response) {
        Tweet tweet=new Tweet(data,null, new Date().getTime(),ControllerUtils.getUserId(request));
        new GenericDB<Tweet>().addRow(tech.codingclub.helix.tables.Tweet.TWEET,tweet);
        return "posted successfully";
    }
    @RequestMapping(method = RequestMethod.POST, value = "/follow-member")
    public
    @ResponseBody
    String followMember(@RequestBody Long memberId, HttpServletRequest request, HttpServletResponse response) {
      Long currentUserId=ControllerUtils.getUserId(request);
      if (currentUserId!=null && memberId!=null && !currentUserId.equals(memberId))
      {
          Follower follower=new Follower(currentUserId,memberId);
          new GenericDB<Follower>().addRow(tech.codingclub.helix.tables.Follower.FOLLOWER,follower);
          return "connected successfully";
      }
      else
      {
          return "not permitted";
      }

    }
    @RequestMapping(method = RequestMethod.GET, value = "/recommendations")
    public String recommendation(ModelMap modelMap, HttpServletResponse response, HttpServletRequest request) {

        Member x=ControllerUtils.getCurrentMember(request);

       List<Member> members= (List<Member>) GenericDB.getRows(tech.codingclub.helix.tables.Member.MEMBER,Member.class, null,10,tech.codingclub.helix.tables.Member.MEMBER.ID.desc());
        modelMap.addAttribute("NAME",x.name);
        modelMap.addAttribute("RECOMMENDATIONS",members);
        return "recommendations";
    }
    @RequestMapping(method = RequestMethod.GET, value = "/followed")
    public String followed(ModelMap modelMap, HttpServletResponse response, HttpServletRequest request) {

        Long currentMemberId=ControllerUtils.getUserId(request);
        Condition condition= tech.codingclub.helix.tables.Follower.FOLLOWER.USER_ID.eq(currentMemberId);
        List<Long> followedId=new GenericDB<Long>().getColumnRows(tech.codingclub.helix.tables.Follower.FOLLOWER.FOLLOWING_ID, tech.codingclub.helix.tables.Follower.FOLLOWER,Long.class,condition,100);
        Condition selectMemberCondition= tech.codingclub.helix.tables.Member.MEMBER.ID.in(followedId);
        List<Member> followedMembers= (List<Member>) GenericDB.getRows(tech.codingclub.helix.tables.Member.MEMBER,Member.class, selectMemberCondition,10,tech.codingclub.helix.tables.Member.MEMBER.ID.desc());
        modelMap.addAttribute("FOLLOWED",followedMembers);
        return "followed";
    }
}