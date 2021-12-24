package com.shop.wear.web;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.wear.constant.ItemSellStatus;
import com.shop.wear.dto.MemberFormDto;
import com.shop.wear.entity.Item;
import com.shop.wear.entity.Member;
import com.shop.wear.entity.QItem;
import com.shop.wear.repository.ItemRepository;
import com.shop.wear.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class HelloController {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    EntityManager em;

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    public void createItemList(){
        for(int i=1;i<=10;i++){
            Item item = new Item();
            item.setItemNm("테스트상품"+i);
            item.setPrice(10000+i);
            item.setItemDetail("테스트 상품 상세 설명"+i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
        }
    }

    @GetMapping("/findByItemDetail")
    public String findByItemDetail(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명");
        for(Item item : itemList){
            System.out.println("findByItemDetail");
            System.out.println(item.toString());
        }
        return "findByItemDetail";
    }

    @GetMapping("/findByItemDetailByNative")
    public String findByItemDetailByNative(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetailByNative("테스트 상품 상세 설명");
        for(Item item : itemList){
            System.out.println("findByItemDetailByNative");
            System.out.println(item.toString());
        }
        return "findByItemDetailNative";
    }

    @GetMapping("/queryDsl1")
    public String queryDsl1(){
        //this.createItemList();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;
        JPAQuery<Item> query = queryFactory.selectFrom(qItem);
        List<Item> itemList = query.fetch();
        for(Item item : itemList){
            System.out.println("테스트");
            System.out.println(item.toString());
        }
        return "queryDsl1";
    }

    @GetMapping("/queryDsl2")
    public String queryDsl2(){
        this.createItemList();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem qitem = QItem.item;
        String itemDetail = "테스트 상품 상세 설명";
        int price = 10003;
        String itemSellStat = "SELL";
        booleanBuilder.and(qitem.itemDetail.like("%"+itemDetail+"%"));
        booleanBuilder.and(qitem.price.gt(price));

        if(StringUtils.equals(itemSellStat, ItemSellStatus.SELL)){
            booleanBuilder.and(qitem.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        PageRequest pageable = PageRequest.of(0,5);
        Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder, pageable);
        System.out.println("total elements :" + itemPagingResult.getTotalElements());
        List<Item> resultItemList = itemPagingResult.getContent();
        for(Item resultItem : resultItemList){
            System.out.println(resultItem.toString());
        }
        return "queryDsl2";
    }

    public Member createMember(){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword("1234");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @GetMapping("/saveMemberTest")
    public String saveMemberTest(){
        Member member = createMember();
        Member savedMember = memberService.saveMember(member);

        System.out.println("member: "+member.getEmail() + "savedMember: " + savedMember.getEmail());
        System.out.println("member: "+member.getName() + "savedMember: " + savedMember.getName());
        System.out.println("member: "+member.getAddress() + "savedMember: " + savedMember.getAddress());
        System.out.println("member: "+member.getPassword() + "savedMember: " + savedMember.getPassword());
        System.out.println("member: "+member.getRole() + "savedMember: " + savedMember.getRole());
        return "queryDsl2";
    }
}
