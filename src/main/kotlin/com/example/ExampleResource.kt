package com.example

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.optaplanner.core.api.domain.entity.PlanningEntity
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty
import org.optaplanner.core.api.domain.solution.PlanningScore
import org.optaplanner.core.api.domain.solution.PlanningSolution
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider
import org.optaplanner.core.api.domain.variable.PlanningVariable
import org.optaplanner.core.api.score.buildin.hardmediumsoftlong.HardMediumSoftLongScore
import org.optaplanner.core.api.score.stream.Constraint
import org.optaplanner.core.api.score.stream.ConstraintFactory
import org.optaplanner.core.api.score.stream.ConstraintProvider

@PlanningSolution
class Solution {
  lateinit var id: String

  @ValueRangeProvider val planningValues: List<Int> = listOf(1, 2, 3)

  @PlanningEntityCollectionProperty //
  lateinit var entities: List<Entity>

  @PlanningScore var score: HardMediumSoftLongScore? = null

  constructor() // required for optaplanner

  constructor(id: String, entities: List<Entity>) {
    this.id = id
    this.entities = entities
  }
}

@PlanningEntity
class Entity {
  @PlanningVariable var planningVar: Int? = null

  /**
   * This triggers a warning
   */
  lateinit var t: Test

  constructor()

  constructor(t: Test) {
    this.t = t
  }
}

@ApplicationScoped
data class Test(val aString: String = "default") {
  val uppercase: String by lazy { aString.uppercase() }
}

class TestConstraintProvider : ConstraintProvider {
  override fun defineConstraints(constraintFactory: ConstraintFactory?): Array<Constraint> {
    return arrayOf()
  }
}

@Path("/hello")
class ExampleResource(
  /**
   * This doesn't trigger a warning
   */
  private val test: Test
) {

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  fun hello(): String {
    val s = Solution("test", listOf(Entity(Test("aString"))))

    return test.uppercase
  }
}
