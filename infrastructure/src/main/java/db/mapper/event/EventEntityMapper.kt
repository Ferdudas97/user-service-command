package org.agh.eaiib.db.mapper.event

import domain.account.model.user.info.FirstName
import domain.account.model.user.info.LastName
import domain.event.model.Event
import domain.event.model.EventId
import domain.event.model.details.*
import domain.event.model.participiant.Age
import domain.event.model.participiant.Guest
import domain.event.model.participiant.Organizator
import domain.event.model.participiant.ParticipantId
import org.agh.eaiib.db.entity.event.DetailsEntity
import org.agh.eaiib.db.entity.event.EventEntity
import org.agh.eaiib.db.entity.event.ParticipiantEntity

fun EventEntity.toDomain() = Event(id = EventId(id),
        details = details.toDomain(),
        guests = guests.map { it.toGuest() }.toSet(), status = status, organizers = organizers.map { it.toOrganizer() }.toSet());

private fun ParticipiantEntity.toGuest() = Guest(ParticipantId(id), FirstName(firstName), LastName(lastName), Age(age))
private fun ParticipiantEntity.toOrganizer() = Organizator(ParticipantId(id), FirstName(firstName), LastName(lastName), Age(age))

private fun DetailsEntity.toDomain() = EventDetails(minimumAgeAllowed = minAllowedAge?.let(::Age),
        maximumAgeAllowed = maxAllowedAge?.let(::Age),
        description = description?.let(::Description),
        localization = Localization(GeoPoint(Longitude(longitude),
                Latitude(latitude))),
        peopleLimit = PeopleLimit(minNumberOfPeople ?: 0, maxNumberOfPeople
                ?: Int.MAX_VALUE),
        category = category,
        period = Period(startDate, endTime))

fun Event.toEntity() = EventEntity(id = id.value,
        guests = guests.map { it.toEntity() }.toSet(),
        organizers = organizers.map { it.toEntity() }.toSet(),
        status = status,
        details = details.toEntity())

private fun EventDetails.toEntity() = DetailsEntity(minAllowedAge = minimumAgeAllowed?.int,
        maxAllowedAge = maximumAgeAllowed?.int,
        maxNumberOfPeople = peopleLimit?.maxNumber,
        minNumberOfPeople = peopleLimit?.minNumber,
        description = description?.text,
        startDate = period.startTime,
        endTime = period.endTime,
        category = category, longitude = localization.point.longitude.value,
        latitude = localization.point.latitude.value)

private fun Guest.toEntity() = ParticipiantEntity(id.id, firstName.value, lastName.value, age.int)
private fun Organizator.toEntity() = ParticipiantEntity(id.id, firstName.value, lastName.value, age.int)