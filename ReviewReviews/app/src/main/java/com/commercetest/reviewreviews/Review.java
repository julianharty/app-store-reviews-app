package com.commercetest.reviewreviews;

import android.os.Build;

import java.net.URL;

/**
 * POJO for review details.
 *
 * Reviews are in UTF16. The specification is available online from Google
 * https://support.google.com/googleplay/android-developer/answer/6135870
 *
 * Timestamps are provided twice in different formats by Google Play Dev Console,
 * a timestamp and the number of milli-seconds since the epoch time. Here the timestamps
 * are stored as strings, that seems good enough for the moment.
 */
public class Review {
    // mandatory
    private String packageName;
    private String reviewSubmitted;
    private int reviewSubmittedMillis;
    private int rating;

    // optional
    private int appVersionCode;
    private String appVersionName;
    private String reviewerLanguage;
    private String device;
    private String lastUpdated;
    private int lastUpdatedMillis;
    private String title;
    private String reviewText;
    private String developerReplied;
    private int developerRepliedMillis;
    private String developerReplyText;
    private URL reviewLink;

    public static class Builder {
        // mandatory in reviews from Google Play Developer Console
        private String packageName;
        private String reviewSubmitted;
        private int reviewSubmittedMillis;
        private int rating;

        // optional in reviews from Google Play Developer Console
        private int appVersionCode;
        private String appVersionName;
        private String reviewerLanguage;
        private String device;
        private String lastUpdated;
        private int lastUpdatedMillis;
        private String title;
        private String reviewText;
        private String developerReplied;
        private int developerRepliedMillis;
        private String developerReplyText;
        private URL reviewLink;

        public Builder(String packageName, String reviewSubmitted, int reviewSubmittedMillis, int rating) {
            this.packageName = packageName;
            this.reviewSubmitted = reviewSubmitted;
            this.reviewSubmittedMillis = reviewSubmittedMillis;
            this.rating = rating;
        }

        public Builder appVersionCode(int appVersionCode) {
            this.appVersionCode = appVersionCode;
            return this;
        }

        public Builder appVersionName(String appVersionName) {
            this.appVersionName = appVersionName;
            return this;
        }

        public Builder reviewerLanguage(String reviewerLanguage) {
            this.reviewerLanguage = reviewerLanguage;
            return this;
        }

        public Builder device(String device) {
            this.device = device;
            return this;
        }

        public Builder lastUpdated(String lastUpdated) {
            this.lastUpdated = lastUpdated;
            return this;
        }

        public Builder lastUpdatedMillis(int lastUpdatedMillis) {
            this.lastUpdatedMillis = lastUpdatedMillis;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder reviewText(String reviewText) {
            this.reviewText = reviewText;
            return this;
        }

        public Builder developerReplied(String developerReplied) {
            this.developerReplied = developerReplied;
            return this;
        }

        public Builder developerRepliedMillis(int developerRepliedMillis) {
            this.developerRepliedMillis = developerRepliedMillis;
            return this;
        }

        public Builder developerReplyText(String developerReplyText) {
            this.developerReplyText = developerReplyText;
            return this;
        }

        public Builder reviewLink(URL reviewLink) {
            this.reviewLink = reviewLink;
            return this;
        }

        public Review build() {
            return new Review(this);
        }
    }

    private Review(Builder builder) {
        this.packageName = builder.packageName;
        this.reviewSubmitted = builder.reviewSubmitted;
        this.reviewSubmittedMillis = builder.reviewSubmittedMillis;
        this.rating = builder.rating;
        this.appVersionCode = builder.appVersionCode;
        this.appVersionName = builder.appVersionName;
        this.reviewerLanguage = builder.reviewerLanguage;
        this.device = builder.device;
        this.lastUpdated = builder.lastUpdated;
        this.lastUpdatedMillis = builder.lastUpdatedMillis;
        this.title = builder.title;
        this.reviewText = builder.reviewText;
        this.developerReplied = builder.developerReplied;
        this.developerRepliedMillis = builder.developerRepliedMillis;
        this.developerReplyText = builder.developerReplyText;
        this.reviewLink = builder.reviewLink;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getReviewSubmitted() {
        return reviewSubmitted;
    }

    public int getReviewSubmittedMillis() {
        return reviewSubmittedMillis;
    }

    public int getRating() {
        return rating;
    }

    public int getAppVersionCode() {
        return appVersionCode;
    }

    public String getAppVersionName() {
        return appVersionName;
    }

    public String getReviewerLanguage() {
        return reviewerLanguage;
    }

    public String getDevice() {
        return device;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public int getLastUpdatedMillis() {
        return lastUpdatedMillis;
    }

    public String getTitle() {
        return title;
    }

    public String getReviewText() {
        return reviewText;
    }

    public String getDeveloperReplied() {
        return developerReplied;
    }

    public int getDeveloperRepliedMillis() {
        return developerRepliedMillis;
    }

    public String getDeveloperReplyText() {
        return developerReplyText;
    }

    public URL getReviewLink() {
        return reviewLink;
    }
}
