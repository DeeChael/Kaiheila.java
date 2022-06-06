package net.deechael.khl.restful.ratelimit;

import net.deechael.khl.restful.RestRoute;

public class RateLimiter {

    public static class Bucket {

        private final RestRoute route;

        public Bucket(RestRoute route) {
            this.route = route;
        }

    }

}
