//
//  RoutesSwift.swift
//  iosApp
//
//  Created by Roberto García Romero on 8/1/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation

import shared

/*enum SplashStateSwift {
    case initialized
    case finished
}

extension RouteSwift {
    init?(_ value: Route) {
        switch value {
        case is SplashState.Init:
            self = .initialized
        case is SplashState.Finished:
            self = .finished
        default:
            return nil
        }
        self = SplashStateSwift.finished
    }
}*/
enum RouteSwift: Hashable {
    case splash
    case onBoarding
    case preferences
    case main
    case talk
    case speaker
}

extension RouteSwift {
    init?(_ value: Routes) {
        switch value {
        case let splash as Routes.Splash:
            self = .splash
        case let onBoarding as Routes.OnBoarding:
            self = .onBoarding
        case let preferences as Routes.FavouriteTracks:
            self = .preferences
        case let mainRoute as Routes.Main:
            self = .main
        case let talk as Routes.Talk:
            self = .talk
        default:
            return nil
        }
    }
}

