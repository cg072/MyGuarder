package home.safe.com.member;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hotki on 2017-12-12.
 */

public class MemberCheck {

    private String name;
    private String pwd;
    private String pwdCheck;
    private String id;
    private char ch;
    private Context context;

    public boolean checkName(String name, Context context) {
        boolean check = false;

        if( name.length() > 0 ) {
            check = true;
        } else {
            Toast.makeText(context, "이름 기입 필수", Toast.LENGTH_SHORT).show();
        }

        return  check;
    }

    public boolean checkPWD(String pwd, String pwdCheck, Context context) {
        boolean check = false;
        int checkNum = 0;
        int checkChar = 0;

        if( pwd.length() >= 7 && pwd.length() <= 20) {
            for( int i = 0 ; i < pwd.length() ; i++) {

                long hexLong = toHex(pwd.charAt(i));

                if(hexLong >= toHex('0') && hexLong <= toHex('9')) { // 숫자
                    checkNum += 1;
                } else if(hexLong >= toHex('a') && hexLong <= toHex('z') || // 소문자 및
                           hexLong >= toHex('A') && hexLong <= toHex('Z')) { // 대문자
                    checkChar += 1;
                }
            }
            if((checkNum > 0 && checkChar > 0) &&           // 혼합 여부 체크
               (checkNum + checkChar) == pwd.length()) {
                if(pwd.equals(pwdCheck)) {
                    check = true;
                } else {
                    Toast.makeText(context, "Password가 서로 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Password는 영숫자 혼합이어야 합니다.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Password 필요 문자는 7 ~ 20 자리 입니다", Toast.LENGTH_SHORT).show();
        }


        return check;
    }

    public boolean checkID(String id, Context context) {
        boolean check = false;
        int checkStr = 0;

        String newID = replaceID(id);
        int checkNum = 0;
        int checkChar = 0;

        boolean firstCheck = false;
        long firstChar = toHex(newID.charAt(0));

        if(firstChar >= toHex('a') && firstChar <= toHex('z')) {
            firstCheck = true;
        }

        // 길이 체크
        if(firstCheck == true) {
            if (newID.length() >= 6 && newID.length() <= 15) {
                for (int i = 0; i < newID.length(); i++) {

                    long hexLong = toHex(newID.charAt(i));

                    if (hexLong >= toHex('0') && hexLong <= toHex('9')) { // 숫자
                        checkNum += 1;
                    } else if (hexLong >= toHex('a') && hexLong <= toHex('z')) {// 소문자
                        checkChar += 1;
                    }
                }

                // 영숫자 혼합으로 이루어졌는지 체크
                if ((checkNum > 0) && (checkChar > 0) &&             // 숫자와 문자가 1글자 이상씩 있고
                        (checkNum + checkChar) == newID.length()) {      // 잡문자 없이, 숫자와 문자의 합이 전체 길이일 때
                    check = true;
                } else {
                    Toast.makeText(context, "영문자, 숫자만 가능", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "ID 필요 문자는 6 ~ 15 자리 입니다.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "ID의 첫글자는 영어 입니다.", Toast.LENGTH_SHORT).show();
        }

        return check;
    }

    private long toHex(char ch) {
        String hexStr = Integer.toHexString(ch);

        Log.v("스트링",String.valueOf(hexStr));

        long hexLong = Long.parseLong(hexStr, 16);

        return hexLong;
    }

    // 대문자를 소문자로 치환하는 메소드
    private String replaceID(String id) {

        return id.toLowerCase();
    }

    // 중복 버튼이 비활성화 되어있는가를 확인하는 메소드
    public boolean checkBtn(String type, boolean check, Context context) {
        boolean btnCheck = check;

        if(btnCheck == true) {
            btnCheck = false;
            Toast.makeText(context, type + " 해주세요", Toast.LENGTH_SHORT).show();
        } else {
            btnCheck = true;
        }
        return btnCheck;
    }

    public boolean checkBirth (String birth, Context context) {
        boolean check = false;
        if(birth.length() == 0) {
            check = true;
        }
        else if(birth.length() == 8) {
            try {
                Double.parseDouble(birth);
                check =  true;
            } catch (NumberFormatException e) {
                Toast.makeText(context, "생년월일은 8자리의 숫자만 입력해주세요(빈칸 가능)", Toast.LENGTH_SHORT).show();
                check =  false;
            }
        } else {
            Toast.makeText(context, "생년월일은 8자리의 숫자만 입력해주세요(빈칸 가능)", Toast.LENGTH_SHORT).show();
            check =  false;
        }

        return check;
    }

    public boolean checkEmail(String email, Context context) {
        boolean check = false;

        // 한개이상 + @ +  한개이상 + 해석금지. + 두개이상
        Pattern p = Pattern.compile("^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])+");
        Matcher m = p.matcher(email);

        if( email.equals("")) {
            check = true;
        }else if ( !m.matches()){
            Toast.makeText(context, "Email형식으로 입력하세요(빈칸가능)", Toast.LENGTH_SHORT).show();

            Log.v("이메일",email + " //" + m.toString());
        } else {
            check = true;
        }

        return check;
    }
}