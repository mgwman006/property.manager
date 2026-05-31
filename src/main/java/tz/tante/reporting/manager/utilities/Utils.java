package tz.tante.reporting.manager.utilities;

public class Utils
{
  public static String normalizePhone(String phone)
  {
    phone = phone.trim().replaceAll("\\s+", "");

    if (phone.startsWith("0"))
    {
      return "+255" + phone.substring(1);
    }

    if (phone.startsWith("255"))
    {
      return "+" + phone;
    }

    if (phone.startsWith("+255")) {
      return phone;
    }

    throw new IllegalArgumentException("Invalid phone number");
  }
}
