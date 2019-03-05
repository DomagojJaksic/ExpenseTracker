package hr.fer.opp.project.enums;

public enum TimePeriod {
    NO_NOTIFICATION {
        @Override
        public String toString() {
            return "Postavke notifikacije nisu odabrane!";
        }
    },
    ONE_WEEK {
        @Override
        public String toString() {
            return "1 tjedan";
        }
    },
    ONE_MONTH {
        @Override
        public String toString() {
            return "1 mjesec";
        }
    },
    THREE_MONTHS {
        @Override
        public String toString() {
            return "3 mjeseca";
        }
    },
    SIX_MONTHS {
        @Override
        public String toString() {
            return "6 mjeseci";
        }
    },
    ONE_YEAR {
        @Override
        public String toString() {
            return "1 godina";
        }
    },
    ALL_TIME {
        @Override
        public String toString() {
            return "Cijelo vrijeme";
        }
    }
}
