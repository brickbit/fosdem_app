//
//  LoadingView.swift
//  iosApp
//
//  Created by Roberto Garcia Romero on 1/10/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct LoadingView: View {
    @State var degreesRotating = 0.0

    var body: some View {
        VStack {
            ZStack {
                Image("LogoInside")
                    .resizable()
                    .frame(width: 50.0, height: 50.0)
                Image("LogoOutside")
                    .resizable()
                    .frame(width: 50.0, height: 50.0)
                    .rotationEffect(.degrees(360))
                    .rotationEffect(.degrees(degreesRotating))
                    .onAppear {
                        withAnimation(.linear(duration: 1)
                            .speed(0.4).repeatForever(autoreverses: false)) {
                                degreesRotating = 360.0
                            }
                    }
            }
            Text("Cargando...").font(.caption)
        }
    }
}

#Preview {
    LoadingView()
}
