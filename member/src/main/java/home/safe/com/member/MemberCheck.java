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

        if( pwd.length() >= 7 ) {
            if(pwd.equals(pwdCheck)) {
                check = true;
            } else {
                Toast.makeText(context, "Password가 서로 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(context, "Password 7자리 이상 요구", Toast.LENGTH_SHORT).show();
        }


        return check;
    }

    public boolean checkID(String id, Context context) {
        boolean check = false;
        int checkStr = 0;

        String newID = replaceID(id);
        int checkNum = 0;
        int checkChar = 0;


        if( newID.length() >= 6 && newID.length() <= 15) {
            for( int i = 0 ; i < newID.length() ; i++) {

                long hexLong = toHex(newID.charAt(i));

                Log.v("차",String.valueOf(newID.charAt(i)));
                Log.v("롱",String.valueOf(hexLong));

                if(hexLong >= toHex('0') && hexLong <= toHex('9')) { // 숫자
                    checkNum += 1;
                } else if(hexLong >= toHex('a') && hexLong <= toHex('z')) {// 소문자
                    checkChar += 1;
                }
            }

            if((checkNum > 0) && (checkChar > 0) &&             // 숫자와 문자가 1글자 이상씩 있고
               (checkNum + checkChar) == newID.length()) {      // 잡문자 없이, 숫자와 문자의 합이 전체 길이일 때
                check = true ;
                Log.v("숫자",String.valueOf(checkNum));
                Log.v("문자",String.valueOf(checkChar));
                Log.v("글자", newID);
                Log.v("길이",String.valueOf(newID.length()));
            } else {
                Log.v("숫자",String.valueOf(checkNum));
                Log.v("문자",String.valueOf(checkChar));
                Log.v("글자", newID);
                Log.v("길이",String.valueOf(newID.length()));
                Toast.makeText(context, "영문자, 숫자만 가능", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "ID 필요 문자 수는 6 ~ 15 개 입니다.", Toast.LENGTH_SHORT).show();
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

        String replace = "";
        String temp;

        for( int i = 0 ; i < id.length() ; i++) {

            temp = String.valueOf(id.charAt(i));

            long hexLong = toHex(id.charAt(i));

            if(hexLong >= toHex('A') && hexLong <= toHex('Z')) {  // 대문자
                hexLong += 32;
                temp = String.valueOf((char)hexLong);
            }
            replace += temp;
        }

        return replace;
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
        if(birth.length() == 8 || birth.length() == 0) {
            try {
                Double.parseDouble(birth);
                return true;
            } catch (NumberFormatException e) {
                Toast.makeText(context, "8자리의 숫자만 입력해주세요(빈칸 가능)", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return check;
    }

    public boolean checkEmail(String email, Context context) {
        boolean check = false;

        Pattern p = Pattern.compile("^[a-zA-X0-9]@[a-zA-Z0-9].[a-zA-Z0-9]");
        Matcher m = p.matcher(email);

        if ( !email.equals("") && !m.matches()){
            Toast.makeText(context, "Email형식으로 입력하세요(빈칸가능)", Toast.LENGTH_SHORT).show();
        } else {
            check = true;
        }

        return check;
    }
}