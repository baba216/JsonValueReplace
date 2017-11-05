package com.shubham.jsonValueAssignment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonValueReplace {

  private static ObjectMapper mapper = new ObjectMapper();

  private static String regex = "\"\\{\\*}\"";

  public static void main(String[] args) throws IOException {
    InputStream exampleInput = JsonValueReplace.class.getClassLoader().getResourceAsStream("query.json");
    JsonNode rootNode = mapper.readTree(exampleInput);
    String x = rootNode.toString();
    String result = assignValues(x,2,"test", Arrays.asList(1,2,3));
    System.out.println(result);
  }

  public static String assignValues(String jsonString, Object... arguments)
      throws JsonProcessingException {
    StringBuilder jsonStringBuilder = new StringBuilder(jsonString);
    Pattern pattern = Pattern.compile(regex);
    for (Object argument : arguments) {
      Matcher matcher = pattern.matcher(jsonStringBuilder);
      if (matcher.find()) {
        int start = matcher.start();
        int end = matcher.end();
        if (argument instanceof String) {
          jsonStringBuilder.replace(start, end, "\"" + String.valueOf(argument) + "\"");
        } else if (argument instanceof Number) {
          jsonStringBuilder.replace(start, end, String.valueOf(argument));
        } else {
          String jsonValue = mapper.writeValueAsString(argument);
          jsonStringBuilder.replace(start,end,jsonValue);
        }
      } else {
        throw new IllegalArgumentException("Arguments not matching placeholder");
      }
    }
    return jsonStringBuilder.toString();
  }
}
