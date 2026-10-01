package tz.tante.property.manager.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
  name = "property_sequences"
)
@Getter
@Setter
@NoArgsConstructor
public class PropertySequence extends BaseEntity
{
  @Column(nullable = false, unique = true)
  private int year;

  @Column(name = "last_sequence", nullable = false)
  private Long lastSequence = 0L;
}
