//
//  injection.swift
//  iosApp
//
//  Created by Roberto García Romero on 8/1/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import Resolver
import shared

extension Resolver: ResolverRegistering {
    @MainActor
    public static func registerAllServices() {
        defaultScope = .application
        
        //Repositories
        //register {ScheduleRepositoryImpl()}.implements(ScheduleRepository.self)

        //Use cases
        /*register {(_, args) in
            GetScheduleDataUseCase(repository: args())
        }*/

        //ViewModels
        /*register {(_, args) in
            SplashViewModel(getSchedule: args())
        }*/

    }
}
