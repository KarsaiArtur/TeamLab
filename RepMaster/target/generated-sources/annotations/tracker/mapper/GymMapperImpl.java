package tracker.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import tracker.dto.GymDto;
import tracker.model.Gym;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-23T21:54:38+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 20 (Oracle Corporation)"
)
@Component
public class GymMapperImpl implements GymMapper {

    @Override
    public GymDto gymToDto(Gym gym) {
        if ( gym == null ) {
            return null;
        }

        GymDto gymDto = new GymDto();

        return gymDto;
    }

    @Override
    public List<GymDto> gymsToDtos(List<Gym> gyms) {
        if ( gyms == null ) {
            return null;
        }

        List<GymDto> list = new ArrayList<GymDto>( gyms.size() );
        for ( Gym gym : gyms ) {
            list.add( gymToDto( gym ) );
        }

        return list;
    }
}
