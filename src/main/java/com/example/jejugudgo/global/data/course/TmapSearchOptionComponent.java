package com.example.jejugudgo.global.data.course;

import com.example.jejugudgo.domain.mygudgo.course.entity.UserJejuGudgoSearchOption;
import com.example.jejugudgo.domain.mygudgo.course.enums.SearchOption;
import com.example.jejugudgo.domain.mygudgo.course.repository.UserJejuGudgoSearchOptionRepository;
import com.example.jejugudgo.global.data.common.entity.DataCommandLog;
import com.example.jejugudgo.global.data.common.repository.DataCommandLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class TmapSearchOptionComponent {
    private final UserJejuGudgoSearchOptionRepository userJejuGudgoSearchOptionRepository;
    private final DataCommandLogRepository dataCommandLogRepository;

    public void createTMAPSearchOption() {
        DataCommandLog checkDataConfig = dataCommandLogRepository.findByConfigKey("TmapSearchOptionData")
                .orElse(null);

        if (checkDataConfig == null || !checkDataConfig.isConfigValue()) {
            for (SearchOption searchOption : SearchOption.values()) {
                UserJejuGudgoSearchOption userJejuGudgoSearchOption = userJejuGudgoSearchOptionRepository.findBySearchOption(searchOption);

                if (userJejuGudgoSearchOption == null) {
                    userJejuGudgoSearchOption = UserJejuGudgoSearchOption.builder()
                            .searchOption(searchOption)
                            .build();

                    userJejuGudgoSearchOptionRepository.save(userJejuGudgoSearchOption);
                }
            }

            DataCommandLog dataCommandLog = DataCommandLog.builder()
                    .configKey("TmapSearchOptionData")
                    .configValue(true)
                    .updatedAt(LocalDate.now())
                    .build();


            dataCommandLogRepository.save(dataCommandLog);
        }


    }
}
