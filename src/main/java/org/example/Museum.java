package org.example;

import java.util.Objects;

public class Museum implements Cloneable {
    private String name;
    private long code;
    private long supervisorCode;
    private Location location;

    private Person manager;
    private Integer foundingYear;
    private String phoneNumber;
    private String fax;
    private String email;
    private String url;
    private String profile;

    // Constructor privat pentru Builder Pattern
    private Museum(MuseumBuilder builder) {
        this.name = builder.name;
        this.code = builder.code;
        this.supervisorCode = builder.supervisorCode;
        this.location = builder.location;
        this.manager = builder.manager;
        this.foundingYear = builder.foundingYear;
        this.phoneNumber = builder.phoneNumber;
        this.fax = builder.fax;
        this.email = builder.email;
        this.url = builder.url;
        this.profile = builder.profile;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public long getCode() {
        return code;
    }
    public void setCode(long code) {
        this.code = code;
    }

    public long getSupervisorCode() {
        return supervisorCode;
    }
    public void setSupervisorCode(long supervisorCode) {
        this.supervisorCode = supervisorCode;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Person getManager() {
        return manager;
    }

    public void setManager(Person manager) {
        this.manager = manager;
    }

    public Integer getFoundingYear() {
        return foundingYear;
    }
    public void setFoundingYear(Integer foundingYear) {
        this.foundingYear = foundingYear;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Museum)) return false;
        Museum museum = (Museum) o;
        return code == museum.code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public Museum clone() {
        try {
            return (Museum) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Museum clone not supported.", e);
        }
    }

    // Builder Pattern
    public static class MuseumBuilder {
        private final String name;
        private final long code;
        private final long supervisorCode;
        private final Location location;

        private Person manager;
        private Integer foundingYear;
        private String phoneNumber;
        private String fax;
        private String email;
        private String url;
        private String profile;

        public MuseumBuilder(String name, long code, long supervisorCode, Location location) {
            if (name == null || location == null) {
                throw new IllegalArgumentException("Name and location cannot be null.");
            }
            if (code <= 0) {
                throw new IllegalArgumentException("Code must be positive.");
            }

            this.name = name;
            this.code = code;
            this.supervisorCode = supervisorCode;
            this.location = location;
        }

        public MuseumBuilder setManager(Person manager) {
            this.manager = manager;
            return this;
        }

        public MuseumBuilder setFoundingYear(Integer foundingYear) {
            this.foundingYear = foundingYear;
            return this;
        }

        public MuseumBuilder setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public MuseumBuilder setFax(String fax) {
            this.fax = fax;
            return this;
        }

        public MuseumBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public MuseumBuilder setUrl(String url) {
            this.url = url;
            return this;
        }

        public MuseumBuilder setProfile(String profile) {
            this.profile = profile;
            return this;
        }

        public Museum build() {
            return new Museum(this);
        }
    }

    public boolean isValid() {
        return code > 0 && foundingYear != null && foundingYear > 0;
    }
}
