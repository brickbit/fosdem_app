//
//  ScheduleItemView.swift
//  iosApp
//
//  Created by Roberto Garcia Romero on 1/10/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct ScheduleItemView: View {
    var schedule: ScheduleBo
    
    var body: some View {
        HStack(alignment: .top) {
            VStack {
                Text("03").foregroundStyle(.white)
                Text(schedule.start).foregroundStyle(.white)
            }
            .frame(height: 135)
            .padding(EdgeInsets(top: 0, leading: 4, bottom: 0, trailing: 4))
            .background(
                Color.black,
                in: RoundedRectangle(
                    cornerRadius: 20,
                    style: .continuous
                )
            )
            VStack(alignment: .leading) {
                Text(schedule.title).padding(EdgeInsets(top: 0, leading: 0, bottom: 16, trailing: 0))
                ForEach(schedule.speaker, id: \.self) { speaker in
                    Text(speaker)
                        .font(.system(size: 12))
                }

            }.padding(EdgeInsets(top: 8, leading: 8, bottom: 8, trailing: 8))
            Spacer()
        }
        .frame(height: 135)
        .cornerRadius(20)
        .overlay(
            RoundedRectangle(cornerRadius: 20)
                .stroke(.black, lineWidth: 2)
        )
    }
}

#Preview {
    ScheduleItemView(schedule: ScheduleBo(id: "", date: "", start: "", duration: "", title: "", subtitle: "", track: "", type: "", language: "", abstract: "", description: "", feedbackUrl: "", attachment: [], speaker: [], room: "", favourite: false, year: ""))
}
