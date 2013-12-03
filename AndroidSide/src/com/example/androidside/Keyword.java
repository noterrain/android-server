package com.example.androidside;

public class Keyword {

    final static public String BROADCAST_FILTER_SERVICE_SCREEN = "com.example.androidside.serviceScreen";
    final static public String BROADCAST_FILTER_MAIN_ACTIVITY = "com.example.androidside.MainActivity";

    final static public String KEYWORD_COMMAND = "command";
    final static public String KEYWORD_COMMAND_GET_DURATION = "get_duration";       // MainActivity --> serviceScreen
    final static public String KEYWORD_COMMAND_REPORT_DURATION = "report_duration"; // serviceScreen --> MainActivity

    final static public String KEYWORD_LAST_SCREENON_TIME = "last_screen_on_time";
    final static public String KEYWORD_LAST_SCREENOFF_TIME = "last_screen_off_time";
    final static public String KEYWORD_LAST_DURATION = "last_duration";

}
