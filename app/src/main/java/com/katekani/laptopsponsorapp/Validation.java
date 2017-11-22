package com.katekani.laptopsponsorapp;

/**
 * Created by Code Tribe on 2017/11/22.
 */
import java.util.ArrayList;
public class Validation {


    public Validation()
    {

    }

    public boolean textEmpty(String text)
    {
        boolean isEmpty = false;

        if(text.isEmpty())
        {
            isEmpty = true;
        }
        else
        {
            isEmpty = false;
        }
        return isEmpty;
    }

    public boolean haveLetter(String num)
    {
        boolean isValid = true;

        for(int x = 0; x<num.length();x++)
        {
            char theChar = num.charAt(x);
            if(Character.isLetter(theChar))
            {
                isValid = false;
            }

            if(Character.isDigit(theChar) == false)
            {
                isValid = false;
            }

        }

        return isValid;
    }


    public boolean shopNameDuplication(String shopName)
    {
        boolean isValid = false;


        return isValid;

    }

    public boolean isPhoneValid(String phoneNo)
    {
        ArrayList<Character> array =new ArrayList<Character>();
        boolean isValid=false;
        int size =phoneNo.length();
        for(int x=0;x<size;x++)
        {
            array.add(phoneNo.charAt(x));
            if(Character.isLetter(array.get(x)))
            {
                isValid=false;
            }
        }

        if(String.valueOf(array.get(0)).equals("0"))
        {
            if (size==10 && Integer.parseInt(String.valueOf(array.get(0)))==0 )
            {
                isValid=true;
            }
        }


        return isValid;
    }


    public boolean isPasswordValid(String passwd)
    {
        ArrayList<Character> array = new ArrayList<Character>();
        boolean isValid=false;
        int size=passwd.length();
        for(int x=0;x<size;x++)
        {
            array.add(passwd.charAt(x));

        }
        boolean haveLatter=false;
        boolean haveNo=false;

        for(int x=0;x<size;x++)
        {
            if(Character.isLetter(array.get(x)))
            {
                haveLatter=true;
            }
            if(Character.isDigit(array.get(x)))
            {
                haveNo=true;
            }
        }

        if(size>4 && haveLatter && haveNo)
        {
            isValid=true;
        }

        return isValid;
    }

    public boolean isNameSurnameValid(String str)
    {
        ArrayList<Character> array=new ArrayList<Character>();

        boolean isValid = true;
        for (int x=0;x<str.length();x++)
        {
            array.add(str.charAt(x));
        }

        for (int x=0;x<array.size();x++)
        {
            if(Character.isDigit(array.get(x)))
            {
                isValid=false;
            }
            else
            {
                if(String.valueOf(array.get(x)).isEmpty())
                {
                    isValid=false;
                }
            }
        }
        return isValid;
    }

    public boolean  isPasswdConfirmPasswdTheSame(String passwd,String confirmPasswd)
    {
        boolean isValid=false;
        if(passwd.equals(confirmPasswd))
        {
            isValid=true;
        }
        return isValid;
    }

    public boolean isEmailValid(String email)
    {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);

        return m.matches();

    }
    ///****validate the role to be selected
    public boolean isRoleValidation(String role)
    {
        boolean valid = false;

        if((role.equalsIgnoreCase("Customer")))
        {
            valid = true;
        }
        else if((role.equalsIgnoreCase("Shop Owner")))
        {
            valid = true;
        }
        else
        {
            valid = false;
        }
        return valid;

    }
    public boolean isPhoneValid2(String phone)
    {
        boolean isValid = false;

        String[] validNum = {"076","071","082","083","073","072","081","079","061","062","060","063","074","078","084","080","086","088","085","077","075","087","089"};

        String num = phone.substring(0, 3);


        for(int x = 0;x<validNum.length;x++)
        {
            if(num.equalsIgnoreCase(validNum[x]))
            {
                isValid = true;
            }
        }
        return isValid;
    }
}
