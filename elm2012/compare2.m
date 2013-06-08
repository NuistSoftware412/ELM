function compare2(  before_file,after_file,x,y )
%COMPARE2 Summary of this function goes here
%   Detailed explanation goes here
d=zeros(2,5);
count=zeros(2,5);

before=load(before_file);
for i=1:size(before,1)
    switch before(i)
        case {1}
            count(1,1)=count(1,1)+1;
        case {2}
            count(1,2)=count(1,2)+1;
        case {3}
            count(1,3)=count(1,3)+1;
        case {4}
            count(1,4)=count(1,4)+1;
        case {5}
            count(1,5)=count(1,5)+1;
    end    
end
after=load(after_file);
for i=1:size(after,1)
    switch after(i)
        case {1}
            count(2,1)=count(2,1)+1;
        case {2}
            count(2,2)=count(2,2)+1;
        case {3}
            count(2,3)=count(2,3)+1;
        case {4}
            count(2,4)=count(2,4)+1;
        case {5}
            count(2,5)=count(2,5)+1;
    end    
end
d(1,:)=count(1,:)/size(before,1);
d(2,:)=count(2,:)/size(after,1);
d=d';
b=bar(d);
legend('Blue: Before Time','Red: After Time');
grid on;
set(gca,'XTickLabel',{'Urban','Water','Vegetation','Arable land','Wetlands'})

end

