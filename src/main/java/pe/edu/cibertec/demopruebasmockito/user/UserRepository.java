package pe.edu.cibertec.demopruebasmockito.user;

interface UserRepository {
    boolean existsByEmail(String email);
    User save(User user);
}

