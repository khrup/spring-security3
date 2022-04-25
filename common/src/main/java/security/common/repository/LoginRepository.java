package security.common.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import security.common.model.MbrInfo;

import java.util.List;

@Mapper
public interface LoginRepository {

    List<MbrInfo> findAllMbrInfo();

    int createMbrInfo(MbrInfo mbrInfo);

    MbrInfo getMbrInfo(MbrInfo mbrInfo);

    MbrInfo getMbrInfoOAuth(MbrInfo mbrInfo);
}
