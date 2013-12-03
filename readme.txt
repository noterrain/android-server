Notes:

- Tomcat 是一個JSP server,用法很簡單,解開以後,在command line 執行Tomcat/bin/startup.sh 就 run 起來了, default port 是 8080.
   Steps:
      i. cd <Tomcat 的目錄>
     ii. ./bin/startup.sh ### Start up Tomcat
    iii. 用browser試試看: http://localhost:8080/  or http://<your ip address>:8080/

- 然後.. 我已經把兩個server side的sample放進去Tomcat了,把Tomcat執行起來以後,測試一下可不可以work

Http Client 的 sample (Tomcat/webapps/example/login.jsp)
    i. http://localhost:8080/example/login.jsp?email=wukon@gmail.com&password=12345
    正常output: Success, (email:wukon@gmail.com, password:12345)

    http://localhost:8080/example/login.jsp?email=wukon@gmail.com&password=12345 這個URL的意思是:
      - http://localhost:8080/example/login.jsp 對這個JSP做動作
      - ? 後面表示參數,有兩個參數: email & password, 值分別是 wukon@gmail.com & 12345

     對應到http://www.androidhive.info/2011/10/android-making-http-requests/ sample的:

     --------------------------------------------------------------------------------
     List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
     nameValuePair.add(new BasicNameValuePair("email", "user@gmail.com"));
     nameValuePair.add(new BasicNameValuePair("password", "encrypted_password"));
     --------------------------------------------------------------------------------

    ### 其他你可以參考我的 android sample,import近來就可以跑了,跑以前記得把code裡的IP換成你Tomcat server的IP


Homework的server side我設計是這樣 (Tomcat/webapps/homework/action.jsp)
        - 參數 action:
              - SET_VALUE_FROM_ANDROID
              - GET_VALUE_FROM_ANDROID
              - SET_VALUE_FROM_ARDUINO
              - GET_VALUE_FROM_ARDUINO)
        - 參數 input: 你可以塞你想要的字串,不過如果有一些符號,要用URL encode. 然後,只有SET_開頭的才有用

用browser test:
    Step 1: http://localhost:8080/homework/action.jsp?action=SET_VALUE_FROM_ANDROID&input=HeLlO
      - 正常output: Success

    Step 2: http://localhost:8080/homework/action.jsp?action=GET_VALUE_FROM_ANDROID
      - 正常output: HeLlO

    Step 3: http://localhost:8080/homework/action.jsp?action=SET_VALUE_FROM_ARDUINO&input=world
      - 正常output: Success

    Step 4:
      - 正常output: world

用法,從 Android 傳值給 Arduino:
1. Android 做 http POST, 參數action=SET_VALUE_FROM_ANDROID, input=<你要的值>
2. Arduino 做 http GET, 參數action=GET_VALUE_FROM_ANDROID

反過來.. 從 Arduino 傳值給 Android:
1. Arduino 做 http POST, 參數action=SET_VALUE_FROM_ARDUINO, input=<你要的值>
2. Android 做 http GET, 參數action=GET_VALUE_FROM_ARDUINO

看不懂,有問題問我啊~ ^^


///////////
=SET_VALUE_FROM_ANDROID ＝>設的值 會在GET_VALUE_FROM_ANDROID拿到
GET_VALUE_FROM_ARDUINO＝>設的值 會在GET_VALUE_FROM_ARDUINO拿到
