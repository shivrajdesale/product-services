package com.shivraj.jobservice.repository;

import com.shivraj.jobservice.enums.Advertiser;
import com.shivraj.jobservice.model.Advert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdvertRepository extends JpaRepository<Advert, String> {
    List<Advert> getAdvertsByUserIdAndAdvertiser(String id, Advertiser advertiser);
}
