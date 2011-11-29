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
    speed = 0.2;
    turn_speed = 0.04;
end

% we assume that the robot is initially facing the +x direction
x = 0;
y = 0;
angle = 0;
dist_thresh = speed;
angle_thresh = 0.1;
% which index we're using as the goal
index = 2;

while(index <= length(px))
    % point towards the next goal
    goal_angle = atan2(py(index)-y, px(index)-x);
    disp(goal_angle);
    AngleSensorRoomba(serPort);
    
    if (goal_angle - angle < 0 ) % Makes sure the robot turns in the right direction
        turnDir = -eps;
    else
        turnDir = eps;
    end
    downsteps = -10000;
    SetFwdVelRadiusRoomba(serPort, turn_speed, turnDir);
    while(abs(goal_angle - angle) > angle_thresh)
        %turnAngle(serPort, turn_speed, 180*(goal_angle-angle)/pi);
        dangle = AngleSensorRoomba(serPort);
        if ~isnan(dangle)
            angle = angle + dangle;
            logdown = log2(abs(goal_angle - angle)/angle_thresh);
            % downsteps monotonically increases (exponent of fraction)
            if(2-logdown >= downsteps)
                downsteps = 2 - logdown;
            end
            if(downsteps > 0)
                SetFwdVelRadiusRoomba(serPort, turn_speed*0.7^downsteps, turnDir);
            end
        else
            disp('angle NaN')
        end
        disp(angle);
    end
    SetFwdVelRadiusRoomba(serPort, 0, inf);
    
    % move towards the next goal
    downsteps = -10000;
    SetFwdVelRadiusRoomba(serPort, speed, inf);
    while(dist([x,y],[px(index),py(index)]) > dist_thresh)
        d = DistanceSensorRoomba(serPort);
        if ~isnan(d)
            x = x + d*cos(angle);
            y = y + d*sin(angle);
            logdown = log2(dist([x,y],[px(index),py(index)])/dist_thresh);
            % downsteps monotonically increases (exponent of fraction)
            if(2-logdown >= downsteps)
                downsteps = 2 - logdown;
            end
            % disp(downsteps);
            % disp([x,y]);
            % disp([px(index),py(index)]);
            if(downsteps > 0)
                SetFwdVelRadiusRoomba(serPort, speed*0.7^downsteps, inf);
            end
        else
            disp('dist NaN')
        end
        disp(d);
        disp(angle);
        
        if isSimulator(serPort)
            pause(0.005);
        end
    end
    SetFwdVelRadiusRoomba(serPort, 0, inf);
    index = index + 1;
end

end

