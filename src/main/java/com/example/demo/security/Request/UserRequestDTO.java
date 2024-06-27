    package com.example.demo.security.Request;

    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class UserRequestDTO {
        private Long id;
        private String lastName;
        private String firstName;
        private String account;
        private String email;
        private String password;

    }
