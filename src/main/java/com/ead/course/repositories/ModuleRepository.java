package com.ead.course.repositories;

import com.ead.course.models.ModuleModel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModuleRepository extends JpaRepository<ModuleModel, UUID>, JpaSpecificationExecutor<ModuleModel> {

    /**
     * Faz com o que seja alterado o tipo de fetch da consulta
     * do campo curso de lazy para eager em tempo de execução
     */
    @EntityGraph(attributePaths = {"course"})
    ModuleModel findByTitle(String title);

    /**
     * {@Query} Usado somente para instruçoes DQL (Data Query Language) customizados
     * <p>
     * {@Modify} juntamente com {@Query} são utilizados quando precisa
     * realizar uma instrução SQL do tipo DML (Data Manipulation Language) customizados
     */
    @Query(value = "select * from TB_MODULES where course_course_id = :courseId", nativeQuery = true)
    List<ModuleModel> findAllModulesIntoCourse(@Param("courseId") UUID courseId);

    @Query(value = "select * from TB_MODULES where course_course_id = :courseId and module_id = :moduleId", nativeQuery = true)
    Optional<ModuleModel> findModuleIntoCourse(@Param("courseId") UUID courseId, @Param("moduleId") UUID moduleId);

}
