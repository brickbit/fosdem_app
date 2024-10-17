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
    
    var notifyEvent: (ScheduleBo) -> Void
    var notNotifyEvent: (ScheduleBo) -> Void
    
    func parseDate(date: String) -> String {
        let newDate = "\(date[5])\(date[6])/\(date[8])\(date[9])"
        return newDate
    }
    var body: some View {
        HStack(alignment: .top) {
            VStack {
                Text(parseDate(date:schedule.date)).foregroundStyle(.white).padding(EdgeInsets(top: 0, leading: 4, bottom: 0, trailing: 4))
                Text(schedule.year).font(.caption).foregroundStyle(.white).padding(EdgeInsets(top: 0, leading: 4, bottom: 16, trailing: 4))
                Text(schedule.start).foregroundStyle(.white)
            }
            .frame(height: 150)
            .padding(EdgeInsets(top: 0, leading: 4, bottom: 0, trailing: 4))
            .background(
                schedule.favourite ? mainGradient : blackGradient
            )
            .clipShape(
                .rect(
                    topLeadingRadius: 20,
                    bottomLeadingRadius: 20,
                    bottomTrailingRadius: 0,
                    topTrailingRadius: 0
                )
            )
            VStack(alignment: .leading) {
                HStack(alignment: .top){
                    Text(schedule.title).padding(EdgeInsets(top: 0, leading: 0, bottom: 16, trailing: 0))
                    Spacer()
                    if(schedule.favourite) {
                        Image(systemName: "heart.fill")
                            .symbolRenderingMode(.hierarchical)
                            .foregroundStyle(mainGradient)
                            .onTapGesture {
                            notNotifyEvent(schedule)
                        }
                    } else {
                        Image(systemName: "heart").onTapGesture {
                            notifyEvent(schedule)
                        }
                    }
                    
                }
                ForEach(schedule.speaker, id: \.self) { speaker in
                    Text(speaker)
                        .font(.system(size: 12))
                }

            }.padding(EdgeInsets(top: 8, leading: 8, bottom: 8, trailing: 8))
            Spacer()
        }
        .frame(width: 350, height: 150)
        .cornerRadius(20)
        .overlay(
            RoundedRectangle(cornerRadius: 20)
                .stroke(.black, lineWidth: 2)
        )
    }
}

#Preview {
    ScheduleItemView(schedule: ScheduleBo(id: "", date: "", start: "", duration: "", title: "", subtitle: "", track: "", type: "", language: "", abstract: "", description: "", feedbackUrl: "", attachment: [], speaker: [], room: "", favourite: false, year: ""), notifyEvent: {schedule in }, notNotifyEvent: {schedule in })
}
