package br.edu.ifgoiano.er;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Aluna: Ana Carolina Silva Borges

class MatchValues {

    private int id;
    private String text;
    private int positionStart;
    private int positionEnd;

    public MatchValues(int id, String text, int positionStart, int positionEnd) {
        this.id = id;
        this.text = text;
        this.positionStart = positionStart;
        this.positionEnd = positionEnd;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public int getPositionStart() {
        return positionStart;
    }

    public int getPositionEnd() {
        return positionEnd;
    }

    @Override
    public String toString() {
        return "{" + id + ", " + text + ", " + positionStart + ", " + positionEnd + "}";
    }
}

class ERRepository {
    private HashMap<Integer, MatchValues> matchValues;
    private String text;
    private String expression;

    public ERRepository(String text, String expression) {
        this.text = text;
        this.expression = expression;
        this.matchValues = new HashMap<>();
    }

    public HashMap<Integer, MatchValues> execute() {
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        int count = 1;
        while (matcher.find()) {
            MatchValues match = new MatchValues(count, text.substring(matcher.start(), matcher.end()), matcher.start(), matcher.end());
            matchValues.put(count, match);
            count++;
        }
        return matchValues;
    }

    public void toPrint() {
        for (MatchValues match : matchValues.values()) {
            System.out.println(match);
        }
    }

    public void toJSON() {
        StringBuilder json = new StringBuilder();
        json.append("[");
        for (MatchValues match : matchValues.values()) {
            json.append("{");
            json.append("\"id\":").append(match.getId()).append(",");
            json.append("\"text\":\"").append(match.getText()).append("\",");
            json.append("\"positionStart\":").append(match.getPositionStart()).append(",");
            json.append("\"positionEnd\":").append(match.getPositionEnd());
            json.append("},");
        }
        if (json.length() > 1) {
            json.deleteCharAt(json.length() - 1); // Remove última vírgula
        }
        json.append("]");
        System.out.println(json.toString());
    }
}

public class Main {
    public static void main(String[] args) {
        String text = "Java é a melhor linguagem de programação do mundo. A linguagem Java ficou em primeiro por anos no TIOBE...";
        String regex = "java";
        
        ERRepository repo = new ERRepository(text, regex);
        
        HashMap<Integer, MatchValues> results = repo.execute();
        
        for (MatchValues match : results.values()) {
            System.out.println("Match: " + match.getId());
            System.out.println(match.getText());
            System.out.println("Position: [" + match.getPositionStart() + ", " + match.getPositionEnd() + "]");
            System.out.println("--------------------------------");
        }
        
        repo.toPrint();
        repo.toJSON();
    }
}

