package uz.pdp.lesson5task1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.pdp.lesson5task1.entity.User;
import uz.pdp.lesson5task1.repository.UsersRepository;


import java.util.Optional;

@Service
public class    AuthService implements UserDetailsService {
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    UsersRepository userRepository;



  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      Optional<User> optionalUser = userRepository.findByEmail(username);
      if(optionalUser.isPresent()) return optionalUser.get();
      throw new UsernameNotFoundException(username+" Topilmadi");
  }
}
