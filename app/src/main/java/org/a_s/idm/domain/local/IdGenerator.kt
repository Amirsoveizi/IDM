package org.a_s.idm.domain.local

import kotlinx.coroutines.flow.Flow

interface IdGenerator {
    suspend fun getId() : Int
}