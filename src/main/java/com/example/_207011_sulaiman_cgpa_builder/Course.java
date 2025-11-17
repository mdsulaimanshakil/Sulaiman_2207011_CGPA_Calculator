package com.example._207011_sulaiman_cgpa_builder;

public record Course(String name, String code, int credit, String teacher1, String teacher2, String grade) {

    public double getGradePoint() {
        return switch (grade) {
            case "A+" -> 4.0;
            case "A" -> 3.75;
            case "A-" -> 3.5;
            case "B+" -> 3.25;
            case "B" -> 3.0;
            case "B-" -> 2.75;
            case "C+" -> 2.5;
            case "C" -> 2.0;
            default -> 0.0;
        };
    }
}