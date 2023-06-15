package com.challenge.devchall.base.initData;

import com.challenge.devchall.item.service.ItemService;
import com.challenge.devchall.photo.service.PhotoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@Profile({"prod"})
public class ItemData {
    @Bean
    CommandLineRunner initData(
            ItemService itemService
    ) {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run (String... args) throws Exception {
                itemService.create("FCE411","font","FCE411", "000000",150);
                itemService.create("basic","font","3D4451", "FFFFFF",0);
                itemService.create("ED3096","font","ED3096", "FFFFFF",300);
                itemService.create("FF9900","font","FF9900", "000000",200);
                itemService.create("1144FC","font","1144FC", "FFFFFF",150);
                itemService.create("3CB24F","font","3CB24F", "FFFFFF",150);

                itemService.create("octopus.png","character","http://iztyfajjvmsf17707682.cdn.ntruss.com/store_img/octopus.png?type=m&w=120&h=120&bgcolor=EBEBEB", " ", 300);
                itemService.create("robot.png","character","http://iztyfajjvmsf17707682.cdn.ntruss.com/store_img/robot.png?type=m&w=120&h=120&bgcolor=EBEBEB", " ", 300);
                itemService.create("ghost.png","character","http://iztyfajjvmsf17707682.cdn.ntruss.com/store_img/ghost.png?type=m&w=120&h=120&bgcolor=EBEBEB", " ", 400);
                itemService.create("cow.png","character","http://iztyfajjvmsf17707682.cdn.ntruss.com/store_img/cow.png?type=m&w=120&h=120&bgcolor=EBEBEB", " ", 500);
                itemService.create("lion.png","character","http://iztyfajjvmsf17707682.cdn.ntruss.com/store_img/lion.png?type=m&w=120&h=120&bgcolor=EBEBEB", " ", 600);
                itemService.create("giraffe.png","character","http://iztyfajjvmsf17707682.cdn.ntruss.com/store_img/giraffe.png?type=m&w=120&h=120&bgcolor=EBEBEB", " ", 700);


            }
        };
    }
}
