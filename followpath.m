function [ ] = followpath( serPort )

% read in the file
filePath = 'path.txt';
file = fileread(filePath);
points = textscan(file, '%f %f');
px = points{1};
py = points{2};

if isSimulator(serPort)
    speed = 0.2;
    turn_speed = 0.2;
else
    speed = 0.05;
    turn_speed = 0.05;
end

% we assume that the robot is initially facing the +x direction
x = 0;
y = 0;
angle = 0;
dist_thresh = speed;
angle_thresh = 0.05;
% which index we're using as the goal
index = 2;

while(index <= length(px))
    % point towards the next goal
    goal_angle = atan2(py(index)-y, px(index)-x);
    disp(goal_angle);
    AngleSensorRoomba(serPort);
    while(abs(goal_angle - angle) > angle_thresh)
        turnAngle(serPort, turn_speed, goal_angle-angle);
        dangle = AngleSensorRoomba(serPort);
        angle = angle + dangle;
    end
    
    % move towards the next goal
    SetFwdVelRadiusRoomba(serPort, speed, inf);
    while(dist([x,y],[px(index),py(index)]) > dist_thresh)
        d = DistanceSensorRoomba(serPort);
        x = x + d*cos(angle);
        y = y + d*sin(angle);
        logdown = log2(dist([x,y],[px(index),py(index)])/dist_thresh);
        downsteps = 2 - logdown;
        disp(downsteps);
        disp([x,y]);
        disp([px(index),py(index)]);
        if(downsteps > 0)
            SetFwdVelRadiusRoomba(serPort, speed*0.5^downsteps, inf);
        end
        if isSimulator(serPort)
            pause(0.05);
        end
    end
    SetFwdVelRadiusRoomba(serPort, 0, inf);
    index = index + 1;
end

end

