package com.example.demo.list.service;



import com.example.demo.list.converter.ListConverter;
import com.example.demo.list.entity.ListEntity;
import com.example.demo.list.entity.UserEntity;
import com.example.demo.list.models.DetailedListDto;
import com.example.demo.list.models.FilterListDto;
import com.example.demo.list.models.ShortListDto;
import com.example.demo.list.repository.ListRepository;
import com.example.demo.list.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@AllArgsConstructor
public class ListServiceImpl {


    @NonNull
    @Lazy
    KafkaTemplate<String, Object> kafkaTemplate;

    @NonNull
    @Lazy
    RestTemplate restTemplate;

    @NonNull
    @Lazy
    ListConverter listConverter;

    @NonNull
    @Lazy
    UserRepository userRepository;

    @NonNull
    @Lazy
    ListRepository listRepository;

    @Transactional
    public synchronized DetailedListDto create(ShortListDto dto){
        System.out.println("AUDIT: start create " + dto.toString());

        sendStatistics(dto);

        validate(dto);

        ListEntity savedList = save(dto);

        System.out.println("AUDIT: end create " + dto.toString());

        DetailedListDto result = listConverter.convert(savedList);
        sendToCore(result);
        return result;

    }

    @Transactional(readOnly = true)
    public synchronized DetailedListDto findOne(UUID id){
        System.out.println("AUDIT: start findOne " + id.toString());

        return listRepository.findById(id)
                .map(listConverter::convert)
                .orElseThrow();
    }

    @Transactional(readOnly = true)
    public synchronized List<ShortListDto> findAll(FilterListDto filterListDto){
        System.out.println("AUDIT: start findAll " + filterListDto.toString());

        return listRepository.findAll()
                .stream()
                .filter(listEntity -> listEntity.getName() == filterListDto.getName())
                .map(listConverter::convertToShort)
                .collect(Collectors.toList());
    }

    @Transactional
    public synchronized void delete(Set<UUID> ids){
        System.out.println("AUDIT: start delete " + ids.toString());

        listRepository.deleteAllById(ids);
    }

    public void sendStatistics(ShortListDto dto){
        restTemplate.postForEntity("https://172.29.30.11:8080/statistics/lists/add",dto,String.class, Map.of());
    }

    public void sendToCore(DetailedListDto dto){
        kafkaTemplate.send("core_topic",dto.toString());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void validate(ShortListDto dto){
        if (dto.getName() == null || dto.getName() == ""){
            throw new RuntimeException("not valid!");
        }

        if (listRepository.existsByName(dto.getName())){
            throw new RuntimeException("not valid!");
        }
    }

    public ListEntity save(ShortListDto dto){
        List<UserEntity> users = userRepository.findAllById(dto.getUsers());

        ListEntity listEntity = listConverter.convert(dto);
        listEntity.setUsers(users);
        ListEntity savedList = listRepository.save(listEntity);

        return savedList;
    }



}

