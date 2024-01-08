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
        

        //Use cases
        
        //Repositories
        
        //ViewModels
        register {SplashViewModel()}

    }
}
