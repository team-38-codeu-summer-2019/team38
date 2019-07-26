package com.google.codeu.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.University;
import com.google.codeu.data.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 * Handles fetching and saving user data.
 */
@WebServlet("/about")
public class AboutMeServlet extends HttpServlet {

    private Datastore datastore;

    @Override
    public void init() {
        datastore = new Datastore();
    }

    /**
     * Responds with the "about me" section for a particular user.
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("universityID", -1);
        jsonObject.addProperty("universityName", "");
        jsonObject.addProperty("aboutMe", "");

        String user = request.getParameter("user");

        if (user == null || user.equals("")) {
            // Request is invalid, return empty response
            response.getWriter().println(jsonObject.toString());
            return;
        }

        User userData = datastore.getUser(user);

        if (userData == null || userData.getUniversity() == -1 && userData.getAboutMe() == null) {
            response.getWriter().println(jsonObject.toString());
            return;
        }

        long universityID = userData.getUniversity();
        University university = datastore.getUniversity(universityID);
        String universityName = "";
        if (university != null) {
            universityName = university.getName();
        }
        String aboutMe = userData.getAboutMe();

        jsonObject.addProperty("universityID", universityID);
        jsonObject.addProperty("universityName", universityName);
        jsonObject.addProperty("aboutMe", aboutMe);

        /*URL url = new URL("https://raw.githubusercontent.com/Hipo/university-domains-list/master/world_universities_and_domains.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        JsonParser jsonParser = new JsonParser();
        JsonArray array_ = jsonParser.parse(br).getAsJsonArray();
        List<String> lst = new ArrayList<>();
        HashSet<String> s = new HashSet<>();
        List<String> APAC_NA = Arrays.asList(
            "AS", "AU", "BD", "BN", "BT", "CC", "CN", "CX", "FJ", "FM",
            "GU", "HK", "ID", "IN", "IO", "JP", "KH", "KI", "KP", "KR",
            "LA", "LK", "MH", "MM", "MN", "MO", "MP", "MV", "MY", "NF",
            "NP", "NR", "NU", "NZ", "PG", "PH", "PK", "PN", "PW", "SB",
            "SG", "TH", "TK", "TL", "TO", "TV", "TW", "UM", "VN", "VU",
            "WS", "CA", "US"
        );
        for (JsonElement jsonElement : array_){
            JsonObject jsonObject1 = (JsonObject)jsonElement;
            String code = jsonObject1.get("alpha_two_code").toString();
            code = code.substring(1, 3);
            if (APAC_NA.contains(code)) {
                universityName = jsonObject1.get("name").toString();
                universityName = universityName.substring(1, universityName.length() - 1);
                universityName = StringEscapeUtils.unescapeJava(universityName);
                if (!s.contains(universityName)) {
                    lst.add(universityName);
                    s.add(universityName);
                }
            }
        }
        Collections.sort(lst);
        int it = 0;
        for (String universityName_ : lst) {
            University university1 = new University(++it, universityName_);
            datastore.storeUniversity(university1);
        }*/

        response.getWriter().println(jsonObject.toString());
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        UserService userService = UserServiceFactory.getUserService();
        if (!userService.isUserLoggedIn()) {
            response.sendRedirect("/index.html");
            return;
        }

        String userEmail = userService.getCurrentUser().getEmail();
        long university = Long.parseLong(request.getParameter("univ-ID"));
        String aboutMe = Jsoup.clean(request.getParameter("about-me"), Whitelist.none());

        User user = new User(userEmail, university, aboutMe);
        datastore.storeUser(user);

        response.sendRedirect("/user-page.html?user=" + userEmail);
    }
}
