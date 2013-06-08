function cart_delve_speedup
   single_time=[2006.019644,1991.913053,1988.804618,1990.403863,1969.616627,1963.029543,1906.168179,1847.538523];
   cluster_6_time=[1137.447912,607.5679895,596.481874,495.025321,470.619881,496.939932,505.3449865,503.729144];
   cluster_8_time=[1052.128249,545.0680405,428.6720085,428.9719815,396.7461995,394.403322,397.173301,393.4042955];
   for i=1:8
       speedup_6(i)=single_time(i)/cluster_6_time(i);
       speedup_8(i)=single_time(i)/cluster_8_time(i);
   end
   N=2:2:16;
   plot(N,speedup_6,'-o',N,speedup_8,'--*');
   xlabel('N');
   ylabel('Speedup');
   title('Speedup of D-ELM on Hadoop Clusters for Regression');
   axis([2 16 1 6]);
   legend('Cluster with 6 Nodes','Cluster wtih 8 Nodes');
end