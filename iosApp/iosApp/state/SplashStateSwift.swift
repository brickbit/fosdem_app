//
//  SplashStateSwift.swift
//  iosApp
//
//  Created by Roberto García Romero on 8/1/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import shared

enum SplashStateSwift {
    case initialized
    case finished(route: RouteSwift)
    case error
}

extension SplashStateSwift {
    init?(_ value: SplashState) {
        switch value {
        case is SplashState.Init:
            self = .initialized
        case let finished as SplashState.Finished:
            self = .finished(route: RouteSwift(finished.route) ?? RouteSwift.onBoarding)
        case is SplashState.Error:
            self = .error
        default:
            return nil
        }
    }
}

