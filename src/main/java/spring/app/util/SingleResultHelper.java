package spring.app.util;

import javax.persistence.*;
import javax.persistence.NoResultException;
import java.util.Optional;

public class SingleResultHelper<T> {

    @SuppressWarnings("unchecked")
    public Optional<T> singleResult(Query query){

        Optional<T> singleResult;
        try {
            singleResult = Optional.of((T) query.getSingleResult());
        } catch (NoResultException noResultException) {
            return Optional.empty();
        }
        return singleResult;
    }
}
