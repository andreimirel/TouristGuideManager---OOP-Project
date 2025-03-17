package org.example;

public class CustomExceptions {
    public static class GuideExistsException extends RuntimeException {
        public GuideExistsException(String message) {
            super(message);
        }
    }

    public static class MemberExistsException extends RuntimeException {
        public MemberExistsException(String message) {
            super(message);
        }
    }

    public static class PersonNotExistsException extends RuntimeException {
        public PersonNotExistsException(String message) {
            super(message);
        }
    }
    public static class GuideTypeException extends RuntimeException {
        public GuideTypeException(String message) {
            super(message);
        }
    }

    public static class GuideNotExistsException extends RuntimeException {
        public GuideNotExistsException(String message) {
            super(message);
        }
    }
    public static class GroupNotExistsException extends RuntimeException {
        public GroupNotExistsException(String message) {
            super(message);
        }
    }
    public static class GroupThresholdException extends RuntimeException {
        public GroupThresholdException(String message) {
            super(message);
        }
    }
}
