package com.example.rent_read.service.auth;

import com.example.rent_read.data.CustomerEntity;
import com.example.rent_read.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  CustomerRepository customerRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    String password = null;
    List<GrantedAuthority> grantedAuthorities = null;

    Optional<CustomerEntity> optional = customerRepository.findOneByEmail(username);
    if (optional.isEmpty()) {
      throw new UsernameNotFoundException("User details not found for the given email: " + username);
    } else {
      CustomerEntity customerEntity = optional.get();
      password = customerEntity.getPassword();
      grantedAuthorities = Set.of(customerEntity.getAuthorityEntity())
          .stream()
          .map(authorityEntity ->
              (GrantedAuthority) new SimpleGrantedAuthority(authorityEntity.getName()))
          .toList();

      return new User(username, password, grantedAuthorities);
    }
  }
}
