//package Modelo;
//import java.util.Locale;
//import org.apache.commons.validator.routines.DateValidator;
//
//
//public class Validaciones{
//    
//    public static boolean dateValidator(String date, String format)
//    {
//        // Obtener la instancia `DateValidator`
//        DateValidator validator = DateValidator.getInstance();
// 
//        // Validar la fecha
//        return validator.isValid(date, Locale.ENGLISH);
//    }
// 
//    // Programa para validar una fecha en Java
//    public static void main(String[] args)
//    {
//        String[] dates = {"01/12/2010", "1/13/2010", "1-13-2010",
//                            "02/29/2016", "02/29/2015" };
// 
//        for (String date: dates)
//        {
//            if (dateValidator(date, "mm/dd/yyyy")) {
//                System.out.println(date + " is valid");
//            }
//            else {
//                System.out.println(date + " isn't valid");
//            }
//        }
//    }
//}