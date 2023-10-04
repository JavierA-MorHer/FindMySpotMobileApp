package com.example.findmyspot.auth.repository

import com.example.findmyspot.auth.model.User

class UserRepository {
        private val personList = mutableListOf<User>()

        // Agregar una persona
        fun addPerson(person: User) {
            personList.add(person)
        }

        // Borrar una persona
        fun removePerson(person: User) {
            personList.remove(person)
        }

        // Actualizar una persona
        fun updatePerson(updatedPerson: User) {
            val existingPerson = personList.find { it.id == updatedPerson.id }
            existingPerson?.let {
                it.firstName = updatedPerson.firstName
                it.lastName = updatedPerson.lastName
                it.email = updatedPerson.email
                it.password = updatedPerson.password

            }
        }

        // Obtener todas las personas
        fun getAllPeople(): List<User> {
            return personList.toList()
        }

}