package cn.smbms.tools;

import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName StringToDateConverter
 * @Description TODO
 * @Author javaboy
 * @Date 2021/1/19 15:20
 * @Version 1.0
 **/
public class StringToDateConverter implements Converter<String, Date> {
    private String pattern;

    public StringToDateConverter(String pattern){
        this.pattern=pattern;
    }

    @Override
    public Date convert(String s) {
        Date date=null;
        try {
            date=new SimpleDateFormat(pattern).parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
