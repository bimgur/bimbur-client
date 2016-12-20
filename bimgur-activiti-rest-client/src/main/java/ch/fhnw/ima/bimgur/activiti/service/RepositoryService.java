package ch.fhnw.ima.bimgur.activiti.service;

import ch.fhnw.ima.bimgur.activiti.model.ProcessDefinition;
import ch.fhnw.ima.bimgur.activiti.model.Deployment;
import ch.fhnw.ima.bimgur.activiti.model.util.ResultList;
import ch.fhnw.ima.bimgur.activiti.service.util.ResultListExtractor;
import io.reactivex.Observable;
import retrofit2.http.GET;

public interface RepositoryService {

    @GET("repository/process-definitions")
    Observable<ResultList<ProcessDefinition>> getProcessDefinitionList();

    default Observable<ProcessDefinition> getProcessDefinition() {
        return ResultListExtractor.extract(getProcessDefinitionList());
    }

    @GET("repository/deployments")
    Observable<ResultList<Deployment>> getDeploymentList();

    default Observable<Deployment> getDeployments() {
        return ResultListExtractor.extract(getDeploymentList());
    }


}