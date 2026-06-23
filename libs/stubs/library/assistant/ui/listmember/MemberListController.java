package library.assistant.ui.listmember;

public class MemberListController {
    public static class Member {
        private String id;
        private String name;
        private String mobile;
        private String email;

        public Member(String id, String name, String mobile, String email) {
            this.id = id;
            this.name = name;
            this.mobile = mobile;
            this.email = email;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getMobile() {
            return mobile;
        }

        public String getEmail() {
            return email;
        }
    }
}
