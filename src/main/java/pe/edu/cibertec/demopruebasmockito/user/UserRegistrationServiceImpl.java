package pe.edu.cibertec.demopruebasmockito.user;

public class UserRegistrationServiceImpl {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserRegistrationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("El correo electrónico ya existe en el sistema");
        }

        String encodedPassword = passwordEncoder.encode(request.rawPassword());
        User newUser = new User(request.email(), encodedPassword);

        return userRepository.save(newUser);
    }
}

