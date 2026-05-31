package tz.tante.reporting.manager.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tz.tante.reporting.manager.repositories.LandlordRepository;
import tz.tante.reporting.manager.repositories.UserRepository;

@Getter
@Setter
@AllArgsConstructor
@Service
public class LandLordService
{
  private final LandlordRepository landlordRepository;
  private final UserRepository userRepository;
  private final RoleService roleService;
  private final PasswordEncoder passwordEncoder;


//  @Transactional
//  public LandLordResponseDto registerLandLord(LandLordRequestDto requestDto)
//  {
//    try
//    {
//      if (landlordRepository.existsByPhoneNumber(requestDto.phoneNumber()))
//      {
//        throw new ResourceExistException("RentalProfile with phone number: " + requestDto.phoneNumber() + " already exists");
//      }
//
//      if (landlordRepository.existsByEmail(requestDto.email()))
//      {
//        throw new ResourceExistException("RentalProfile with email: " + requestDto.email() + " already exists");
//      }
//
//
//      RentalProfile newLandLord = new RentalProfile();
//
//      User user = userRepository.findByUsername(requestDto.phoneNumber())
//        .orElse(null);
//
//      if (user == null)
//      {
//        String encodedPassword = passwordEncoder.encode(requestDto.passWord());
//
//        user = new User(requestDto.phoneNumber(), encodedPassword);
//        BusinessMembershipRole landlordRole = roleService.getRoleByName(BusinessMembershipRoleName.ROLE_LANDLORD.toString());
//        user.addRole(landlordRole);
//
//        userRepository.save(user);
//      }
//
//      newLandLord.setUser(user);
//      user.setRentalProfileProfile(newLandLord);
//
//      newLandLord = landlordRepository.save(newLandLord);
//
//      return null;
//    }
//    catch (Exception exception)
//    {
//      throw new TanteException(exception.getMessage());
//    }
//
//  }


}
