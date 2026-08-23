package tz.tante.property.manager.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Address
{
  private long postalCode;

  private long streetNumber;

  private String streetName;

  private String ward;

  private String city;

  private String region;

  private String country;

  private String popularAreaName;
}
