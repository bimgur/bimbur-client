package ch.fhnw.ima.bimgur.activiti.service.util;

import ch.fhnw.ima.bimgur.activiti.model.util.ResultList;
import io.reactivex.Observable;

public interface ResultListExtractor {
    static <T> Observable<T> extract(Observable<ResultList<T>> observableResultList) {
        return observableResultList.flatMap(resultList -> Observable.fromIterable(resultList.getData()));
    }
}